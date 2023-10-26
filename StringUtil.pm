package StringUtil;

sub new {
    my $class = shift;
    my $self = {};
    bless $self, $class;
    return $self;
}

sub reverse_string {
    my ($self, $string) = @_;
    return scalar reverse $string;
}

sub uppercase_string {
    my ($self, $string) = @_;
    return uc $string;
}

sub lowercase_string {
    my ($self, $string) = @_;
    return lc $string;
}

sub remove_whitespace {
  my ($self, $string) = @_;
  $string =~ s/\s+//g;
  return $string;
}

sub count_words {
  my ($self, $string) = @_;
  my @words = split /\s+/, $string;
  return scalar @words;
}

sub string_to_int {
  my ($self, $string) = @_;
  return int $string;
}

sub string_to_float {
  my ($self, $string) = @_;
  return sprintf("%.2f", $string);
}

sub int_to_string {
  my ($self, $int) = @_;
  return "$int";
}

sub float_to_string {
  my ($self, $float) = @_;
  return sprintf("%.2f", $float);
}
1;