package DBUtil;

use DBI;
use Carp qw(croak);

sub new {
    my ($class, $dsn, $user, $password) = @_;
    croak "Missing required input: dsn" unless defined $dsn;
    croak "Missing required input: user" unless defined $user;
    croak "Missing required input: password" unless defined $password;
    my $self = {
        dsn => $dsn,
        user => $user,
        password => $password,
        dbh => undef
    };
    bless $self, $class;
    return $self;
}

sub connect {
    my ($self) = @_;
    return if defined $self->{dbh};
    eval {
        $self->{dbh} = DBI->connect($self->{dsn}, $self->{user}, $self->{password}, {
            RaiseError => 1,
            PrintError => 0,
            AutoCommit => 1
        });
    };
    croak "Could not connect to database: $@" if $@;
}

sub disconnect {
    my ($self) = @_;
    return unless defined $self->{dbh};
    $self->{dbh}->disconnect();
    $self->{dbh} = undef;
}

sub execute_query {
    my ($self, $query, @params) = @_;
    croak "No database handle exists" unless defined $self->{dbh};
    croak "Missing required input: query" unless defined $query;
    my $sth = $self->{dbh}->prepare_cached($query)
        or croak "Could not prepare query: " . $self->{dbh}->errstr;
    eval {
        $sth->execute(@params)
            or croak "Could not execute query: " . $sth->errstr;
    };
    croak "Could not execute query: $@" if $@;
    my @rows;
    while (my $row = $sth->fetchrow_hashref()) {
        push @rows, $row;
    }
    return \@rows;
}

sub get_column_names {
    my ($self, $table_name) = @_;
    croak "No database handle exists" unless defined $self->{dbh};
    croak "Missing required input: table_name" unless defined $table_name;
    my $quoted_table_name = $self->{dbh}->quote_identifier($table_name);
    my $sth = $self->{dbh}->prepare("SELECT column_name FROM all_tab_columns WHERE table_name = $quoted_table_name")
        or croak "Could not prepare query: " . $self->{dbh}->errstr;
    eval {
        $sth->execute()
            or croak "Could not execute query: " . $sth->errstr;
    };
    croak "Could not execute query: $@" if $@;
    my @column_names;
    while (my $row = $sth->fetchrow_hashref()) {
        push @column_names, $row->{column_name};
    }
    return \@column_names;
}

sub get_table_names {
    my ($self) = @_;
    croak "No database handle exists" unless defined $self->{dbh};
    my $sth = $self->{dbh}->prepare("SELECT table_name FROM all_tables")
        or croak "Could not prepare query: " . $self->{dbh}->errstr;
    eval {
        $sth->execute()
            or croak "Could not execute query: " . $sth->errstr;
    };
    croak "Could not execute query: $@" if $@;
    my @table_names;
    while (my $row = $sth->fetchrow_hashref()) {
        push @table_names, $row->{table_name};
    }
    return \@table_names;
}

sub insert_row {
    my ($self, $table_name, $data) = @_;
    croak "No database handle exists" unless defined $self->{dbh};
    croak "Missing required input: table_name" unless defined $table_name;
    croak "Missing required input: data" unless defined $data;
    my $quoted_table_name = $self->{dbh}->quote_identifier($table_name);
    my $column_names = join(", ", map { $self->{dbh}->quote_identifier($_) } keys %$data);
    my $placeholders = join(", ", map { "?" } values %$data);
    my $sth = $self->{dbh}->prepare_cached("INSERT INTO $quoted_table_name ($column_names) VALUES ($placeholders)")
        or croak "Could not prepare query: " . $self->{dbh}->errstr;
    eval {
        $sth->execute(values %$data)
            or croak "Could not execute query: " . $sth->errstr;
    };
    croak "Could not execute query: $@" if $@;
}

sub update_row {
    my ($self, $table_name, $data, $where) = @_;
    croak "No database handle exists" unless defined $self->{dbh};
    croak "Missing required input: table_name" unless defined $table_name;
    croak "Missing required input: data" unless defined $data;
    croak "Missing required input: where" unless defined $where;
    my $quoted_table_name = $self->{dbh}->quote_identifier($table_name);
    my $set_clause = join(", ", map { $self->{dbh}->quote_identifier($_) . " = ?" } keys %$data);
    my $where_clause = join(" AND ", map { $self->{dbh}->quote_identifier($_) . " = ?" } keys %$where);
    my $sth = $self->{dbh}->prepare_cached("UPDATE $quoted_table_name SET $set_clause WHERE $where_clause")
        or croak "Could not prepare query: " . $self->{dbh}->errstr;
    eval {
        $sth->execute(values %$data, values %$where)
            or croak "Could not execute query: " . $sth->errstr;
    };
    croak "Could not execute query: $@" if $@;
}

sub delete_row {
    my ($self, $table_name, $where) = @_;
    croak "No database handle exists" unless defined $self->{dbh};
    croak "Missing required input: table_name" unless defined $table_name;
    croak "Missing required input: where" unless defined $where;
    my $quoted_table_name = $self->{dbh}->quote_identifier($table_name);
    my $where_clause = join(" AND ", map { $self->{dbh}->quote_identifier($_) . " = ?" } keys %$where);
    my $sth = $self->{dbh}->prepare_cached("DELETE FROM $quoted_table_name WHERE $where_clause")
        or croak "Could not prepare query: " . $self->{dbh}->errstr;
    eval {
        $sth->execute(values %$where)
            or croak "Could not execute query: " . $sth->errstr;
    };
    croak "Could not execute query: $@" if $@;
}

sub partition {
  my ($self, $table_name, $column_name, $num_partitions) = @_;
  croak "No database handle exists" unless defined $self->{dbh};
  croak "Missing required input: table_name" unless defined $table_name;
  croak "Missing required input: column_name" unless defined $column_name;
  croak "Missing required input: num_partitions" unless defined $num_partitions && $num_partitions > 0;
  my $quoted_table_name = $self->{dbh}->quote_identifier($table_name);
  my $sth = $self->{dbh}->prepare("SELECT MIN($column_name), MAX($column_name) FROM $quoted_table_name")
    or croak "Could not prepare query: " . $self->{dbh}->errstr;
  eval {
    $sth->execute()
      or croak "Could not execute query: " . $sth->errstr;
  };
  croak "Could not execute query: $@" if $@;
  my ($min_value, $max_value) = $sth->fetchrow_array();
  my $range = ($max_value - $min_value) / $num_partitions;
  my @partitions;
  for (my $i = 0; $i < $num_partitions; $i++) {
    my $start_value = $min_value + $i * $range;
    my $end_value = $min_value + ($i + 1) * $range;
    my $where_clause = "$column_name >= ? AND $column_name < ?";
    push @partitions, {
      name => "partition_$i",
      where => $where_clause,
      params => [$start_value, $end_value]
    };
  }
  return \@partitions;
}

1;