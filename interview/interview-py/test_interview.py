
from __future__ import absolute_import, division, print_function, unicode_literals

import unittest
from interview import step

class StepTestCase(unittest.TestCase):
    """Tests for `interview.py`."""

    def test_a(self):
        self.assertEqual(step('a'),'a')

    def test_aa(self):
        self.assertEqual(step('aa'),'')

    def test_aaa(self):
        self.assertEqual(step('aaa'),'')

    def test_aba(self):
        self.assertEqual(step('aba'),'b')

    def test_abba(self):
        self.assertEqual(step('abba'),'')

    def test_ababa(self):
        self.assertEqual(step('ababa'),'')

if __name__ == "__main__":
    unittest.main()