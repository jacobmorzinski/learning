#![feature(lookup_host)]

use std::collections::HashMap;
use std::collections::hash_map::Entry;
use std::io::Result;
use std::net::SocketAddr;

#[derive(Debug)]
struct DnsLookupCache {
    cache: HashMap<String, Vec<SocketAddr>>,
}

impl DnsLookupCache {
    fn new() -> DnsLookupCache {
        DnsLookupCache {
            cache: HashMap::new(),
        }
    }

    fn lookup(&mut self, host: &str) -> Result<&Vec<SocketAddr>> {
        match self.cache.entry(host.to_owned()) {
            Entry::Occupied(e) => Ok(e.into_mut()),
            Entry::Vacant(e)   => Ok(e.insert(do_lookup(host)?))
        }
    }
}

fn do_lookup(host: &str) -> Result<Vec<SocketAddr>> {
    Ok(std::net::lookup_host(host)?.collect())
}

fn main() {
    let mut dlc = DnsLookupCache::new();
    {let res1 = dlc.lookup("mit.edu"); println!("{:?}", res1);}
    {let res2 = dlc.lookup("mit.ed"); println!("{:?}", res2);}
}
