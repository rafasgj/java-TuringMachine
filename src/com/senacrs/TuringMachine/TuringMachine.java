package com.senacrs.TuringMachine;

import java.util.Scanner;

/**
 *  Implements a Turing Machine with the following characteristics
 *  - Programs run in an infinite loop.
 *  - Programs may have code blocks. Each code block run in an infinite loop.
 *  - Machine always start at the first tape position.
 *  - Tape size is limited to 10 (ten) positions. (TODO: configurable)
 *  - Commands:
 *  	> : move to the next tape position.
 *  	< : move to the previous tape position.
 *  	+ : add 1 to the current tape position.
 *  	- : subtract 1 from the current tape position.
 *  	{ : start a new code block.
 *  	} : end the current code block.
 *  	? : execute the next command or code block if the current tape
 *  		position is not equal to 0 (zero).
 * 		x : break out of the current code block.
 */
public class TuringMachine {

	public static void main(String[] args) {
		(new TuringMachine(10)).run();
	}

	int head = 0;
	private int[] tape;
	
	private TuringMachine(int tapeSize) {
		this.tape = new int[tapeSize];
	}
	
	private void run() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Enter program code:");
			String program = sc.nextLine();
			
			System.out.println("Enter program input (<0 to stop):");
			for (int i = 0; i < tape.length; i++) {
				int value = sc.nextInt();
				if (value < 0) {
					for (; i < tape.length; i++)
						tape[i] = 0;
					break;
				}
				tape[i] = value;
			}
			// The annoying Scanner nextInt bug!
			sc.nextLine();
			try {
				execute(program);
			} catch (TuringException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private void execute(String program) throws TuringException {
		// Sanitize program text
		program = program.replace(" ", "");
		System.out.println("Executing: " + program);
		// setup machine
		int[] blockStack = new int[1 + tape.length / 2];
		int lvl = 0;
		int pc = 0;
		head = 0;
		// run it
		try {
			while (true) {
				char command = program.charAt(pc++);
				// show current status
				System.out.println("Command: " + command + "\n" + this);
				// execute command
				switch (command) {
					case '>': head++; break;
					case '<': head--; break;
					case '+': tape[head]++; break;
					case '-': tape[head]--; break;
					case '{': blockStack[lvl++] = pc; break;
					case '}': pc = blockStack[lvl-1]; break;
					case '?':
						if (tape[head] != 0) {
							if (program.charAt(pc) == '{')
								pc = blockEnd(program, pc+1);
							else
								pc++; 
						}
						break;
					case 'x':
						if (lvl == 0) return;
						lvl--;
						pc = blockEnd(program, pc) + 1;
						break;
					default:
						throw new TuringException("Invalid command", program, pc-1);
				}
				
				if (pc >= program.length())
					pc = 0;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new TuringException("Reached the end of tape.", program, pc-1);
		}
	}
	
	private int blockEnd(String program, int pc) throws TuringException {
		try {
			int count = 1;
			while (count > 0) {
				switch (program.charAt(pc)) {
					case '}': count--; break;
					case '{': count++; break;
					default: pc++;
				}
			}
		} catch (Exception e) {
			throw new TuringException("Block not closed.", program);
		}
		return pc;
	}

	@Override
	public String toString() {
		String res = "";
		for (int x = 0; x < tape.length; x++) {
			if (x == head) res += " *";
			else res += " |";
			res += " " + tape[x];
		}
		return res + " |";
	}

}
