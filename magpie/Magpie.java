/*
    Title           :   Natural Language Interpreter
    Author          :   ALELG, MUSTAFA
    Date            :   2021-03-31
    Description     :   Arithmetica is a chatbot that answers simple math and arithmetic questions
						Features:
							1. Auto Suggestions
							2. Understands minor input errors (e.g: heello)
							3. Easily extensible through Response class
							4. Math calculations
							5. Formula substitutor
*/

import java.util.*;
public class Magpie {

	public static void startChat() {
		Scanner in = new Scanner(System.in);

		String prevEntry = null;
		String entry = "hi";
		Response resp = getResponse(entry);
		
		while (resp != resps[0]) {
			System.out.println(resp.composeAns(entry)+"\n");

			if (!entry.equals("__REPEATED"))
				prevEntry = entry;
			entry = in.nextLine();
			if (entry.equals(prevEntry) && resp != resps[2])
				entry = "__REPEATED";

			resp = getResponse(entry);
		}
		resp = getResponse("exit");
		System.out.println(resp.composeAns("exit"));

		in.close();
	}

	public static Response getResponse(String in) {
		for (Response r : resps) {
			if (r.respondsTo(in)) {
				return r;
			}
		}
		return null;
	}
	public static Response[] resps = {
		///////////////////////////
		//  Elementary messages  //
		///////////////////////////

		new Response(new String[] { // Exit message
				"All right. Good bye.",
				"I will shut down",
				"OK, Bye.",
			},
			new String[] {"exit|quit|bye|goodbye"}
		),
		new Response("Simple Personal Interactions:\n\t\te.g: how are you? what is your name?",
				new String[] {
				"Hello, my name is Arithmetica. Ask me any simple math questions.",
				"Hi, How are you?",
				"Greetings!",
				"I am Arithmetica, what should I call you?",
			},
			new String[] {"hi+|h+e+l+o+"}
		),
		new Response(new String[] { // Exit message
				"Looks like you entered an empty message.",
				"...",
				"Go ahead and ask me",
				"Don't be shy. There are no stupid questions.",
			},
			new String[] {},
			new String[] {"^\s*$"}
		),
		new HelpResponse("Help: shows this help menu.",
			new String[] { 
				"\nArithmetica is a math chatbot. It can do the following: just ask the bot! \n\n"
			},
			new String[] {"he+l+p+|(i ask)"}
		),
		///////////////////////////////////////
		//  Icebreakers & Personal Messages  //
		///////////////////////////////////////

		new Response(new String[] {
				"My name is Arithmetica!",
				"I am Arithmetica. Glad to meet you!"
			},
			new String[] {"your name"}
		),
		new DateResponse("Current Date",
				new String[] {"Today's date is: "},
				new String[] {"date"}
		),
		new Response(new String[] { // How are you
					"I am doing well. What about you?",
					"I am good. Thank you!",
			},
			new String[] {"how are you"}
		),
		new Response(new String[] { // Glad to hear
					"Glad to hear that!",
					"That's great!",
					"I hope you the best!",
			},
			new String[] {"(is|are|im|iam|i am|i'm)\s+(good|fine|great|nice)"}
		),
		new Response(new String[] {  // Tasleek
					"Good.",
					"Ok.",
					"That is nice",
					"Glad to hear",
					"Awesome",
			},
			new String[] {"(me too)|(you too)|ok|good|great|nice|fine|yeah|yes|yup"}
		),
		new Response(new String[] {  // Negative
					"Why not?",
					"Why negative?",
					"Ok.",
			},
			new String[] {"nah|no"}
		),
		new Response(new String[] { // Thanks
					"You are welcome :)",
			},
			new String[] {"thanks*|thx|ty"}
		),
		new Response(new String[] { 
					"That's a nice name!",
					"Nice to meet you",
			},
			new String[] {"im|i am|i'm|my name is"}
		),
		new Response(new String[] { 
					"Tell me more about your family.",
					"Who is this person?",
			},
			new String[] {"mother|father|brother|sister|aunt|uncle|family"}
		),

		/////////////////////////////////
		//  Middle school mathematics  //
		/////////////////////////////////

		new Response("Order of operation",
				new String[] { 
					"The order of operations, PEMDAS:\n"
					+"\n"
					+"1. Paranethesis\n"
					+"2. Exponentiation and root extraction\n"
					+"3. Multiplication and division\n"
					+"4. Addition and subtraction\n"
			},
			new String[] {"order", "operation"}
		),
		new Response("Quadratic Equation",
				new String[] {
					"The solution to a quadratic equation of the form ax^2 + bx"
					+ "c = 0 is\n x = (-b +- sqrt(b^2-4*c*a))/2",
			},
			new String[] {"quadratic", "equation"}
		),
		////////////////
		//  Geometry  //
		////////////////

		new MathResponse("Circle area\n\t\tEx: What is the area of circle with radius 8?",
			new String[] {
				"Area of a circle with radius %s is %s",
				"%0",
				"%1*%0^2"
			},
			new String[] {"area", "circle"},
			new String[] {"r", "pi"},
			new double[] {0, Math.PI}
		),
		new MathResponse("Circle Perimeter",
			new String[] {
				"Perimeter of a circle with radius %s is %s",
				"%0",
				"2*%1*%0"
			},
			new String[] {"perimeter", "circle"},
			new String[] {"r", "pi"},
			new double[] {0, Math.PI}
		),
		new MathResponse("Triangle Area",
			new String[] {
				"Area of a triangle with base %s and height %s is %s",
				"%0",
				"%1",
				"0.5*%1*%0"
			},
			new String[] {"area", "triangle"},
			new String[] {"b", "h"},
			new double[] {0, 0}
		),
		new MathResponse("Rectangle Area",
			new String[] {
				"Area of a rectangle with width %s and height %s is %s",
				"%0",
				"%1",
				"%1*%0"
			},
			new String[] {"area", "triangle"},
			new String[] {"w", "h"},
			new double[] {0, 0}
		),
		new MathResponse("Rectangle perimeter",
			new String[] {
				"Area of a rectangle with width %s and height %s is %s",
				"%0",
				"%1",
				"%1*%0"
			},
			new String[] {"area", "rectangle"},
			new String[] {"w", "h"},
			new double[] {0, 0}
		),
		new MathResponse("Cylinder volume",
			new String[] {
				"Volume of a cylinder with base radius %s and height %s is %s",
				"%0",
				"%1",
				"%2*%1*%0^2"
			},
			new String[] {"volume", "cylinder"},
			new String[] {"r", "h", "pi"},
			new double[] {0, 0, Math.PI}
		),
		new MathResponse("Sphere volume",
			new String[] {
				"Volume of a sphere with base radius %s is %s",
				"%0",
				"4/3*%1%0^2"
			},
			new String[] {"volume", "sphere"},
			new String[] {"r", "pi"},
			new double[] {0, Math.PI}
		),
		new MathResponse("Cone volume",
			new String[] {
				"Volume of a cone with base radius %s and height %s is %s",
				"%0",
				"%1",
				"1/3*%1*%0^2"
			},
			new String[] {"volume", "cone"},
			new String[] {"r", "h", "pi"},
			new double[] {0, 0, Math.PI}
		),
		new MathResponse("Temperature conversion: C --> F \n\t\t(command: to f)",
			new String[] {
				"%s degrees Celsius is %s degrees Fahrenheit",
				"%0",
				"1.8*%0+32"
			},
			new String[] {"to (f|fahrenheit)"},
			new String[] {"t"},
			new double[] {0}
		),
		new MathResponse("Temperature conversion: F --> C \n\t\t(command: to c)",
			new String[] {
				"%s degrees Fahrenheit is %s degrees Celsius",
				"%0",
				"(%0-32)*5/9"
			},
			new String[] {"to (c|celsius)"},
			new String[] {"t"},
			new double[] {0}
		),

		//////////////////////////////
		//  Low Priority Responses  //
		//////////////////////////////

		new MathResponse("Basic arithmetic calculations:\n\t\t Try: (12+17)^2"),
		new Response(new String[] { // Repeated lines
					"I have already told you the answer",
					"Why do you keep repeating yourself?",
					"Same as before.",
			},
			new String[] {"__REPEATED"}
		),
		new SuggestResponse(new String[] {
			"Try to be more specific. Here are some suggestions of similar commands:\n",
			"Sorry, didn't get it. I suggest the following commands:\n",
		}),
		new Response(new String[] { // No response
				"Sorry, I don't understand. Type Help to view available commands.",
			}
		),
		
	};

	public static void main(String[] args) {
		startChat();
	}
}
