use strict;
use warnings;
use parent 'Animal';

# Define the Cat class
package Overrides::Dog;

use Exporter;

our @ISA = qw(Exporter);
our @EXPORT_OK = qw(new make_sound);

# Constructor method
sub new {
    my $class = shift;
    my $self = $class->SUPER::new(@_);
    $self->{sound} = 'Woof';
    bless $self, $class;
    return $self;
}

# Override the make_sound method
sub make_sound {
    my $self = shift;
    return $self->{sound};
}

1;
