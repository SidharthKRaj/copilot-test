package Lion;

use parent 'Animal';

# Constructor method
sub new {
    my $class = shift;
    my $self = $class->SUPER::new(@_);
    $self->{SOUND} = 'ROAR';
    bless $self, $class;
    return $self;
}

# Override the make_sound method
sub make_sound {
    my $self = shift;
    return $self->{SOUND};
}

1;
