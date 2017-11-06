# Can I make an "agent" using rust?

For example:

* ssh-agent holds sensitive information
  https://github.com/openssh/openssh-portable/blob/master/ssh-agent.c
* lastpass-agent holds sensitive information
  https://github.com/lastpass/lastpass-cli

A brief note about daemonizing a rust app is at:

https://github.com/rust-lang/rust/issues/15641

## Answer

It is complicated; the pseudo-code pasted in the 2014 rust-lang issue no long works and the "detach" feature was removed from `command`.

## Additional

Learn from https://github.com/Muterra/py_daemoniker