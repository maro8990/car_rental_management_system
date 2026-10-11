package java_project;

//Enum gia tis proairetikes prosthetes ipiresies pou mporei na exei mia enoikiasi.
//Kathe ipiresia ilopoiei ton diko tis tropo ipologismou kostous
public enum AdditionalService {
    GPS {
        //Override
        public double calculateCost(int days) {
        	//To GPS xrewneetai 5 euro ana mera.
            return 5 * days;
        }
    },
    CHILD_SEAT {
        //Override
        public double calculateCost(int days) {
        	// To paidiko kathisma exei statheri xreosi 20 euro gia oli ti diarkeia.
            return 20;
        }
    },
    EXTRA_INSURANCE {
        //Override
    	// I prostheti asfaleia xreonetai 30 euro ana periodo 5 imeron
        public double calculateCost(int days) {
        	//Math.ceil gia na kanei stroggilopoiisi pros ta pano
            int periods = (int) Math.ceil(days / 5.0);
            return periods * 30;
        }
    };
	
	// Abstract methodos pou ipologizei to kostos tis kathe ipiresias
    // me vasi tis meres tis enoikiasis
    public abstract double calculateCost(int days);
}