package com.app.model;

public enum Indicators {

	ROTATION{
		@Override
		public String toString() {
			return "Rotación";
		}
	},
	INVENVTARY_DAYS{
		@Override
		public String toString() {
			return "Dias de inventario";
		}
	},
	COVER{
		@Override
		public String toString() {
			return "Cobertura";
		}
	}, 
	FAULTS{
		@Override
		public String toString() {
			return "Faltantes";
		}
	},
	
	
}
