package FTPUtil;

use Net::FTP;
use Carp qw(croak);

sub new {
    my ($class, $host, $user, $password) = @_;
    croak "Missing required input: host" unless defined $host;
    croak "Missing required input: user" unless defined $user;
    croak "Missing required input: password" unless defined $password;
    my $self = {
        host => $host,
        user => $user,
        password => $password,
        ftp => undef
    };
    bless $self, $class;
    return $self;
}

sub connect {
    my ($self) = @_;
    return if defined $self->{ftp};
    my $retry_count = 0;
    while ($retry_count < 3) {
        eval {
            $self->{ftp} = Net::FTP->new($self->{host}, Passive => 1)
                or croak "Could not connect to FTP server: $@";
            $self->{ftp}->login($self->{user}, $self->{password})
                or croak "Could not login to FTP server: " . $self->{ftp}->message;
            $self->{ftp}->binary();
        };
        last unless $@;
        $retry_count++;
        warn "Error connecting to FTP server: $@. Retrying...\n";
        sleep 5;
    }
    croak "Could not connect to FTP server after $retry_count retries" unless defined $self->{ftp};
}

sub disconnect {
    my ($self) = @_;
    return unless defined $self->{ftp};
    $self->{ftp}->quit();
    $self->{ftp} = undef;
}

sub upload_file {
    my ($self, $local_file, $remote_file) = @_;
    croak "No FTP connection exists" unless defined $self->{ftp};
    croak "Missing required input: local_file" unless defined $local_file;
    croak "Missing required input: remote_file" unless defined $remote_file;
    my $retry_count = 0;
    while ($retry_count < 3) {
        eval {
            $self->{ftp}->put($local_file, $remote_file)
                or croak "Could not upload file: " . $self->{ftp}->message;
        };
        last unless $@;
        $retry_count++;
        warn "Error uploading file: $@. Retrying...\n";
        sleep 5;
    }
    croak "Could not upload file after $retry_count retries" if $retry_count == 3;
}

sub download_file {
    my ($self, $remote_file, $local_file) = @_;
    croak "No FTP connection exists" unless defined $self->{ftp};
    croak "Missing required input: remote_file" unless defined $remote_file;
    croak "Missing required input: local_file" unless defined $local_file;
    my $retry_count = 0;
    while ($retry_count < 3) {
        eval {
            $self->{ftp}->get($remote_file, $local_file)
                or croak "Could not download file: " . $self->{ftp}->message;
        };
        last unless $@;
        $retry_count++;
        warn "Error downloading file: $@. Retrying...\n";
        sleep 5;
    }
    croak "Could not download file after $retry_count retries" if $retry_count == 3;
}

1;