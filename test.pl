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
    # if ($row->{NAME} eq 'cat') {
    #     my $cat = Lion->new($row->{NAME}, $row->{TYPE});
    #     my $sql = $dbh->prepare("INSERT INTO ANIMAL
    #                     (NAME, TYPE) values (?, ?)");
    #     $sql->execute($cat->{NAME}, $cat->{TYPE}) or die $DBI::errstr;
    #     $sql->finish();
    # }
}

my $ipv4 = "10.1.1.90";
print "$ipv4 valid\n\n" if (Utils::validate_ipv4_address($ipv4));

# copilot auto generated this code. even recognized the reference and dereferenced it. 
# Also used the right ipv8 validate function name.
my @ipv6_addresses = @{IPV6_ADDRESSES()};
foreach my $ipv6 (@ipv6_addresses) {
    print "$ipv6 is valid\n" if (Utils::validate_ipv6_address($ipv6));
}


