#!/usr/bin/python

from objects import *
from database import *
from core import *
from menu import *

####
#### M A I N
######################################################

class Main:
    def main(self):
        db = DB()

        dbload = DatabaseLoader()
        dbload.load(db)

        m = Menu(db)
        m.start()

        print'done.'

####
#### I N I T I A L   L A U N C H E R
######################################################
m = Main()
m.main()



