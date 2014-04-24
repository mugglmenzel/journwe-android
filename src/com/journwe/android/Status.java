package com.journwe.android;

public enum Status {
	GOING{
		@Override
		public String toString() {
			return "Going";
		}
	},
	UNDECIDED{
		@Override
		public String toString() {
			return "Undecided";
		}
	},
	NOTGOING{
		@Override
		public String toString() {
			return "Not Going";
		}
	},
	BOOKED{
		@Override
		public String toString() {
			return "Booked";
		}
	},
	
}
