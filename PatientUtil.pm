package PatientUtil;

use strict;
use warnings;
use Carp qw(croak);

sub new {
    my ($class, %params) = @_;
    my @required_params = qw(first_name last_name dob gender address phone_number);
    foreach my $param (@required_params) {
        croak "Missing required input: $param" unless defined $params{$param};
    }
    my $self = \%params;
    bless $self, $class;
    return $self;
}

sub get_first_name {
    my ($self) = @_;
    return $self->{first_name};
}

sub get_last_name {
    my ($self) = @_;
    return $self->{last_name};
}

sub get_dob {
    my ($self) = @_;
    return $self->{dob};
}

sub get_gender {
    my ($self) = @_;
    return $self->{gender};
}

sub get_address {
    my ($self) = @_;
    return $self->{address};
}

sub get_phone_number {
    my ($self) = @_;
    return $self->{phone_number};
}

sub get_email {
    my ($self) = @_;
    return $self->{email};
}

sub get_blood_type {
    my ($self) = @_;
    return $self->{blood_type};
}

sub get_height {
    my ($self) = @_;
    return $self->{height};
}

sub get_weight {
    my ($self) = @_;
    return $self->{weight};
}

sub validate_insurance {
    my ($self) = @_;
    my $insurance = $self->{insurance};
    # Check if the insurance is valid
    unless (defined $insurance) {
        croak "Missing required input: insurance";
    }
    unless ($insurance =~ /^[A-Z0-9]{10}$/) {
        croak "Invalid insurance: $insurance";
    }
    return 1;
}

1;