package ParseCSV;

use Text::CSV;

use Exporter qw(import);

our @ISA = qw(Exporter);
our @EXPORT_OK = qw(read_csv);

sub read_csv {
    my $filename = shift;

    my $csv = Text::CSV->new({ binary => 1 }) or die "Cannot use CSV: " . Text::CSV->error_diag();
    open(my $fh, "<:encoding(utf8)", $filename) or die "Could not open '$filename': $!";

    my @rows;
    while (my $row = $csv->getline($fh)) {
        push @rows, {NAME => $row->[0], TYPE => $row->[1]};
    }
    shift @rows;

    $csv->eof or $csv->error_diag();
    close $fh;

    return \@rows;
}

1;
