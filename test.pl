#!/usr/bin/perl
use strict;
use warnings;
use lib 'Modules';

use Database;
use ParseCSV;
use Animal;
use Lion;
use DBI;
use Utils;
use CONSTANTS 'IPV6_ADDRESSES';

# Connect to the database
my $dbh = Database::connect_to_database();

use Data::Dumper;
# print Dumper($dbh);

my $rows = ParseCSV::read_csv("Data/animals.csv");

foreach my $row (@$rows) {
    if ($row->{NAME} eq 'cat') {
        my $cat = Lion->new($row->{NAME}, $row->{TYPE});
        my $sql = $dbh->prepare("INSERT INTO ANIMAL (NAME, TYPE) values (?, ?)");
        $sql->execute($cat->{NAME}, $cat->{TYPE}) or die $DBI::errstr;
        $sql->finish();
    }
}

my $ipv4 = "10.1.1.90";
print "$ipv4 valid\n\n" if (Utils::validate_ipv4_address($ipv4));

# copilot auto generated this code. even recognized the reference and dereferenced it. 
# Also used the right ipv8 validate function name.
my @ipv6_addresses = @{IPV6_ADDRESSES()};
foreach my $ipv6 (@ipv6_addresses) {
    print "$ipv6 is valid\n" if (Utils::validate_ipv6_address($ipv6));
}

# comment the below code if running this perl code. Its to test copilot conversion capabilities
my $data = Utils::Get_Data();

my ($ltsvc) = grep { $_->{CPT} =~ /LT/ } @{$data};
my ($ltdenial) = grep { $_->{CODE} = 'IT25' && $ltsvc->{BILLED} == $_->{AMOUNT} } @{$ltsvc->{CODES}};
my ($lcpt, $lmod) = split(/,/, $ltrsvc->{CPT});
my ($rtsvc) = grep { $_->{CPT} =~ /RT/ } @{$data};
my ($rtdenial) = grep { $_->{CODE} = 'IT25' && $rtsvc->{BILLED} == $_->{AMOUNT} } @{$rtsvc->{CODES}};
my ($rcpt, $rmod) = split(/,/, $rtsvc->{CPT});
my ($ltpay, $rtpay) = (0) * 2;
my ($svc50, $cpt50, $mod50);

if ($lcpt == $$rcpt && $ltdenial && $rtdenial) {
    $svc50 = grep { $_->{CPT} =~ /$rcpt, 50/ } @{$data};
    ($cpt50, $mod50) = split(/,/, $svc50->{CPT});
    if ($mod50 eq '50') {
        $ltpay = $svc50->{PAYMENT} / 2;
        $rtpay = Utils::Sum_Currency($svc50->{PAYMENT}, -$ltpay);
        foreach my $code (@{$svc50->{CODES}}) {
            $rtcodeamount = $code->{AMOUNT} / 2;
            $ltcodeamount = Utils::Sum_Currency($code->{AMOUNT}, - $rtcodeamount);
            $code->{SVCID} = $svc50->{ID};
            Utils::Add_Code($dbh, $code);
        }

        my $fake = {
            CODE => 'FAKE',
            AMOUNT => 0,
            SVCID => $svc50->{ID}
        }

        Utils::Add_Code($dbh, $fake);
    }
}