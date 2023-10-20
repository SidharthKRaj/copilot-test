package Animal;

use Exporter;

our @ISA = qw(Exporter);
our @Export = qw(new make_sound);

sub new {
    my $class = shift;
    my $self = {
        NAME => shift,
        TYPE => shift,
    };
    bless $self, $class;
    return $self;
}

sub make_sound {
    my $self = shift;
    return 'unknown sound';
}

1;
