import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;

public class YugiohSearchFilter {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        File inputFile = new File("C:\\Users\\Daniel\\Documents\\NetBeansProjects\\YugiohSearchFilter\\src\\yugiohsearchfilter\\GoatFormatCards.txt"); //Alter your file location accordingly
        Scanner scanOb = new Scanner(inputFile);
        
        //spells and traps can only have one identifier so they only need one HashMap
        HashMap<String, List<String>> spells = new HashMap<>();
        HashMap<String, List<String>> traps = new HashMap<>();

        //Monsters have multiple identifiers so a HashMap for each identifier is needed 
        HashMap<String, List<String>> attribute = new HashMap<>();
        HashMap<String, List<String>> monsterType = new HashMap<>();
        
        //class and subtype can be combined
        HashMap<String, List<String>> monsterSubType = new HashMap<>();

        HashMap<Integer, List<String>> level = new HashMap<>(); 
        HashMap<Integer, List<String>> atk = new HashMap<>(); 
        HashMap<Integer, List<String>> def = new HashMap<>(); 
        
        //1748 cards
        scanOb.nextLine(); //Skip the header information at the top of the file. 
       
    while (scanOb.hasNextLine()){ //loop through the text file: 
        String currentLine = scanOb.nextLine();
        int i=0;
        int j=0;
        int nextSlot = 0;
        
        String[] cardInfo = new String[9]; /*max number of identifiers for a card is 9
        No class:    Name [0], Card Type [1], Card sub type [2], attribute [3], monster type [4], level [5], atk[6], def[7].
        Has class:   Name [0], Card Type [1], Card sub type [2], attribute [3], monster type [4], class[5], level [6], atk[7], def[8].
        Spells and traps will only fill 3 spaces*/
        
        //Parse the current line for commas and fill the info array. 
            while (j<currentLine.length()){ 
                if (currentLine.charAt(j) == ',' && nextSlot<9){
                    cardInfo[nextSlot] = currentLine.substring(i,j);
                    
                    nextSlot++;
                    
                    i = j+1; //Skip over the comma we just ran into
                    j = i;
                }
                j++;
            }
            
            try {
            //The array space for monster levels was picking up an extra comma for monsters without a class. Monsters with a class
            //have their level, atk, and def in a different space than monsters without so we need to alter and shift values. 
            if(cardInfo[5].contains(",")){ //Deletes the comma from monsters without a class
               cardInfo[5] = cardInfo[5].substring(1);
            } else { //Shift values for monsters with a class so information remains consistent for all monsters
               cardInfo[2] = cardInfo[2].concat("-").concat(cardInfo[5]);
               cardInfo[5] = cardInfo[6];
               cardInfo[6] = cardInfo[7];
               cardInfo[7] = cardInfo[8];
            }
            }
            catch(NullPointerException e) {}
            
            /*      Print the contents of the array to confirm everything has been properly filled
            for (int p=0; p<8; p++){
                System.out.println(cardInfo[p]);
            }
           */
            
       try{ 
            if (cardInfo[1].equals("Spell")){
                if (!spells.containsKey(cardInfo[2])){
                    spells.put(cardInfo[2], new ArrayList<>());
                }
                spells.get(cardInfo[2]).add(cardInfo[0]);
            }
            
            else if (cardInfo[1].equals("Trap")){
                if (!traps.containsKey(cardInfo[2])){
                    traps.put(cardInfo[2], new ArrayList<>());
                }
                traps.get(cardInfo[2]).add(cardInfo[0]);
            }
            
            else if (cardInfo[1].equals("Monster")){
              //Name [0], Card Type [1], monsterSubType [2], attribute [3], monsterType [4], level [5], atk[6], def[7]
              
                if (!monsterSubType.containsKey((cardInfo[2]))){
                    monsterSubType.put((cardInfo[2]), new ArrayList<>());
                }
                    monsterSubType.get(cardInfo[2]).add(cardInfo[0]);
                    
                //Flip, Spirit, Toon, and Union class monsters are subTypes of effect so they will also be added to Effect.         
                if (cardInfo[2].equals("Effect-Flip") || cardInfo[2].equals("Effect-Spirit") || cardInfo[2].equals("Effect-Toon")|| cardInfo[2].equals("Effect-Union")){
                  monsterSubType.get("Effect").add(cardInfo[0]);
                }
                
                if (!attribute.containsKey(cardInfo[3])){
                    attribute.put(cardInfo[3], new ArrayList<>());
                }
                    attribute.get(cardInfo[3]).add(cardInfo[0]);
                
                if (!monsterType.containsKey((cardInfo[4]))){
                    monsterType.put((cardInfo[4]), new ArrayList<>());
                }
                    monsterType.get(cardInfo[4]).add(cardInfo[0]);
                    
                if (!level.containsKey(Integer.parseInt(cardInfo[5]))){
                    level.put(Integer.parseInt(cardInfo[5]), new ArrayList<>());
                }
                    level.get(Integer.parseInt(cardInfo[5])).add(cardInfo[0]);
                   
                if (!atk.containsKey(Integer.parseInt(cardInfo[6]))){
                    atk.put(Integer.parseInt(cardInfo[6]), new ArrayList<>());
                }
                    atk.get(Integer.parseInt(cardInfo[6])).add(cardInfo[0]);
                    
                if (!def.containsKey(Integer.parseInt(cardInfo[7]))){
                    def.put(Integer.parseInt(cardInfo[7]), new ArrayList<>());
                }
                    def.get(Integer.parseInt(cardInfo[7])).add(cardInfo[0]);
            } 
       }
       catch (NullPointerException e) {}  
    }

    HashSet<String> findMonsters = new HashSet<>(level.get(8)); //Edit level if you want to sort by a different parameter
    
    List<String>[] identifiers = new ArrayList[2]; //Edit array size based on how many values you want to filter by. Max size is 5
    identifiers[0] = attribute.get("Light");
    identifiers[1] = monsterType.get("Dragon"); //comment out values accordingly to prevent NullPointerException
  //  identifiers[2] = monsterSubType.get("Effect");
  //  identifiers[3] = atk.get(450);
  //  identifiers[4] = def.get(1000);
    
    for (int i=0; i<identifiers.length; i++){
  
        HashSet<String> filterCards = new HashSet<>(identifiers[i]);
        findMonsters.retainAll(filterCards);
    }
    System.out.println(findMonsters);

    /* Other sample searches
    System.out.println(spells.get("Quick-Play"));
    System.out.println(traps.get("Counter"));
    System.out.println(traps.values());
    */
  }  
}
 
