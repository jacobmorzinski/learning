'''
Inspired by a comment on -c help:
given a string, return the first unique non-repeated character in the string

One answer uses a memo.
This answer is one-pass streaming.
'''
from __future__ import absolute_import, division, print_function, unicode_literals

import collections

def step(letters, seen_once=None, seen_more=None):
    """Step through string `letters` and discover the first unique non-repeated character."""
    if seen_once is None:
        seen_once = collections.OrderedDict()
    if seen_more is None:
        seen_more = set()

    if letters == '':
        if seen_once:
            return next(iter(seen_once))
        else:
            return ''
    else:
        (cur, rest) = (letters[0], letters[1:])
        if cur in seen_more:
            pass
        elif cur in seen_once:
            seen_once.pop(cur)
            seen_more.add(cur)
        else:
            seen_once[cur] = True
        return step(rest, seen_once, seen_more)

def main():
    """Main entry point"""
    i = 'aabcbccd'
    map(print, step(i))

if __name__ == "__main__":
    main()
