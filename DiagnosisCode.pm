package DiagnosisCode;

use strict;
use warnings;
use Carp qw(croak);

my %diagnosis_codes = (
    'A00' => 'Cholera',
    'A01' => 'Typhoid and paratyphoid fevers',
    'A02' => 'Other salmonella infections',
    # ...
);

sub new {
    my ($class, $code) = @_;
    croak "Missing required input: code" unless defined $code;
    my $self = {
        code => $code
    };
    bless $self, $class;
    return $self;
}

sub validate {
    my ($self) = @_;
    my $code = $self->{code};
    # Check if the code is alphanumeric and has a length of 3
    unless ($code =~ /^[A-Z0-9]{3}$/) {
        croak "Invalid diagnosis code: $code";
    }
    # Check if the first character is a letter
    unless ($code =~ /^[A-Z]/) {
        croak "Invalid diagnosis code: $code";
    }
    # Check if the second and third characters are digits between 0 and 9
    unless ($code =~ /^[A-Z][0-9]{2}$/) {
        croak "Invalid diagnosis code: $code";
    }
    return 1;
}

sub get_description {
    my ($code) = @_;
    my $description = $diagnosis_codes{$code};
    unless (defined $description) {
        croak "Invalid diagnosis code: $code";
    }
    return $description;
}

1;