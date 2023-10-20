package Database;

use strict;
use warnings;
use DBI;
use Exporter qw(import);

our @ISA = qw(Exporter);
our @EXPORT_OK= qw(connect_to_database);

sub connect_to_database {
    my $driver = "mysql"; 
    my $database = "copilot";
    my $dsn = "DBI:$driver:database=$database";
    my $username = "root";
    my $password = "admin123";

    my $dbh = DBI->connect($dsn, $username, $password) or die "Could not connect to database: $DBI::errstr";

    return $dbh;
}

1;
