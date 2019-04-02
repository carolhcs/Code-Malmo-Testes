/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
//package expmalmomeca1;
import com.microsoft.msr.malmo.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.json.simple.JSONObject;
import org.json.*;
/**
*
* @author carol
*/

public class ExpMalmoMeca5 {

   static
   {
       System.loadLibrary("MalmoJava"); // attempts to load MalmoJava.dll (on Windows) or libMalmoJava.so (on Linux)
   }
   
   /**
    * @param argv the command line arguments
    */
       
   public static void main(String argv[]){
       // TODO code application logic here
          
       //XML da missão
       //+"<ContinuousMovementCommands turnSpeedDegs=\"180\"/>" ou +"<DiscreteMovementCommands/>"
       String missionXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalon=\"no\" ?>"
               +"<Mission xmlns=\"http://ProjectMalmo.microsoft.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                   +"<About><Summary>Hello World in Java!</Summary></About>"
                   +"<ServerSection>"
                       +"<ServerInitialConditions>"
                           +"<Time><StartTime>1000</StartTime><AllowPassageOfTime>false</AllowPassageOfTime></Time>"
                           +"<Weather>clear</Weather>"
                       +"</ServerInitialConditions>"
                       +"<ServerHandlers>"
                           +"<FlatWorldGenerator generatorString=\"3;7,44*49,73,35:1,159:4,95:13,35:13,159:11,95:10,159:14,159:6,35:6,95:6;12;\"/>"
                           +"<DrawingDecorator>"
                               +"<DrawSphere x=\"-27\" y=\"69\" z=\"0\" radius=\"30\" type=\"air\"/>"
                               +"<DrawCuboid x1=\"-25\" y1=\"39\" z1=\"-2\" x2=\"-29\" y2=\"39\" z2=\"2\" type=\"lava\"/>"
                               +"<DrawCuboid x1=\"-26\" y1=\"39\" z1=\"-1\" x2=\"-28\" y2=\"39\" z2=\"1\" type=\"obsidian\"/>"
                               +"<DrawBlock x=\"-27\" y=\"39\" z=\"0\" type=\"diamond_block\"/>"
                           +"</DrawingDecorator>"
                           +"<ServerQuitFromTimeUp timeLimitMs=\"30000\"/>"
                           +"<ServerQuitWhenAnyAgentFinishes/>"
                       +"</ServerHandlers>"
                   +"</ServerSection>"
                   +"<AgentSection mode=\"Survival\">"
                       +"<Name>MalmoTutorialBot</Name>"
                       +"<AgentStart>"
                           +"<Placement x=\"0.5\" y=\"56.0\" z=\"0.5\" yaw=\"90\"/>"
                           +"<Inventory><InventoryItem slot=\"8\" type=\"diamond_pickaxe\"/></Inventory>"
                       +"</AgentStart>"
                       +"<AgentHandlers>"
                           +"<ObservationFromFullStats/>"
                           +"<ObservationFromGrid>"
                               +"<Grid name=\"floor3x3\">"
                                   +"<min x=\"-1\" y=\"-1\" z=\"-1\"/>"
                                   +"<max x=\"1\" y=\"-1\" z=\"1\"/>"
                               +"</Grid>"
                           +"</ObservationFromGrid>"
                           +"<ContinuousMovementCommands turnSpeedDegs=\"180\"/>"
                           +"<InventoryCommands/>"
                           +"<AgentQuitFromTouchingBlockType>"                      
                               +"<Block type=\"diamond_block\" />"
                           +"</AgentQuitFromTouchingBlockType>"
                       +"</AgentHandlers>"
                   +"</AgentSection>"
               +"</Mission>";
       
       
       AgentHost agent_host = new AgentHost(); //Criar agente
       try {
           StringVector args = new StringVector();
           //args.add("JavaExamples_run_mission");
           args.add("ExpMalmoMeca5");
           for( String arg : argv )
               args.add( arg );
           agent_host.parse( args );
       }
       catch( Exception e ) {
           System.err.println( "ERROR: " + e.getMessage() );
           System.err.println( agent_host.getUsage() );
           System.exit(1);
       }
       if( agent_host.receivedArgument("help") ) {
           System.out.println( agent_host.getUsage() );
           System.exit(0);
       }        
               
       MissionSpec my_mission;
       try {
           my_mission = new MissionSpec(missionXML, true);
           MissionRecordSpec my_mission_record = new MissionRecordSpec();
           my_mission.setSummary("Primeiro Experimento de Missão");
           
           try {
               agent_host.startMission( my_mission, my_mission_record );
           }
           catch (MissionException e) {
               System.err.println( "Error starting mission: " + e.getMessage() );
               System.err.println( "Error code: " + e.getMissionErrorCode() );
               // We can use the code to do specific error handling, eg:
               if (e.getMissionErrorCode() == MissionException.MissionErrorCode.MISSION_INSUFFICIENT_CLIENTS_AVAILABLE)
               {
                   // Caused by lack of available Minecraft clients.
                   System.err.println( "Is there a Minecraft client running?");
               }
               System.exit(1);
           }
           
       } catch (Exception ex) {
           Logger.getLogger(ExpMalmoMeca5.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       
       //Loop until mission starts:
       WorldState world_state;
       System.out.print( "Waiting for the mission to start" );
       do {
           System.out.print( "." );
           try {
               Thread.sleep(100);//Thread.sleep(10);
           } catch(InterruptedException ex) {
               System.err.println( "User interrupted while waiting for mission to start." );
               return;
           }
           world_state = agent_host.getWorldState();
           for( int i = 0; i < world_state.getErrors().size(); i++ )
               System.err.println( "Error: " + world_state.getErrors().get(i).getText() );
       } while( !world_state.getIsMissionRunning() );
       System.out.println( "" );
       System.out.println( "Mission running" );
       
       int delay = 50;   // 5000 delay de 5 seg.
       int interval = 10;  // 1000 intervalo de 1 seg.
       Timer timer = new Timer();
       
       //timer.scheduleAtFixedRate(new TimerTask() {
       //@Override
       //public void run() {
       //Commands 
       agent_host.sendCommand("hotbar.9 1");//Press the hotbar key
       agent_host.sendCommand("hotbar.9 0");//Release hotbar key - agent should now be holding diamond_pickaxe
       agent_host.sendCommand("pitch 0.2");//Start looking downward slowly
       try {
           Thread.sleep(1);//Wait a second until we are looking in roughly the right direction
       } catch (InterruptedException ex) {
           Logger.getLogger(ExpMalmoMeca5.class.getName()).log(Level.SEVERE, null, ex);
       }
       agent_host.sendCommand("pitch 0");//Stop tilting the camera
       agent_host.sendCommand("move 0.3");//And start running...
       agent_host.sendCommand("attack 1");//Whilst flailing our pickaxe!
       //Commands End
       //}
       //}, delay, interval);

       
       boolean pular = false;
       
       //Loop until mission ends:
       do {
           
           try {
               Thread.sleep((long) 0.1);
           } catch (InterruptedException ex) {
               Logger.getLogger(ExpMalmoMeca5.class.getName()).log(Level.SEVERE, null, ex);
           }
           world_state = agent_host.getWorldState();
           for( int i = 0; i < world_state.getErrors().size(); i++ ) {
               TimestampedString error = world_state.getErrors().get(i);
               System.err.println( "Error: " + error.getText() );
           }
           /*
           System.out.print( "video,observations,rewards received: " );
           System.out.print( world_state.getNumberOfVideoFramesSinceLastState() + "," );
           System.out.print( world_state.getNumberOfObservationsSinceLastState() + "," );
           System.out.println( world_state.getNumberOfRewardsSinceLastState() );
           for( int i = 0; i < world_state.getRewards().size(); i++ ) {
               TimestampedReward reward = world_state.getRewards().get(i);
               System.out.println( "Summed reward: " + reward.getValue() );
           }
           */
           //Parcialmente funcionando
           if (world_state.getNumberOfObservationsSinceLastState() > 0){
               TimestampedStringVector observations = world_state.getObservations();
               if (observations.isEmpty())System.out.println("Observation Vazio!!");
               String obs_text = observations.get((int) (observations.size() - 1)).getText();
               JSONObject observation = new JSONObject(obs_text);
               JSONArray blocks = observation.getJSONArray("floor3x3");
               String grid[] = {"","","","","","","","",""};
               //Ver o que há em todas as posições do array
               
               //timer.scheduleAtFixedRate(new TimerTask() {
               //@Override
               //public void run() {
               for (int i = 0; i < 9; ++i) {
                   String block = blocks.getString(i);
                   grid[i] = block;
                   System.out.println("Posição: "+ i + "=" +block+ "\n");
                   //if (grid [i] == "lava") grid[3]="lava";
               }
               //}
               //}, delay, interval);
               //grid[3]="lava";

               if ("lava".equals(grid[3])){
                   //agent_host.sendCommand("move 0");
                   agent_host.sendCommand("jump 1");
                   pular = true;
                   System.out.println("PULOU \n PULOU");
               }
               else if (!pular && (!"lava".equals(grid[4]))){
                   //agent_host.sendCommand("move 1");
                   agent_host.sendCommand("jump 0");
                   pular = false;
                   System.out.println("PAROU DE PULAR \n PAROU DE PULAR");
               }
               
               //------
               /*TimestampedString msg = world_state.getObservations().get(0);
               String msg2 = msg.getText();
               JSONObject observations = new JSONObject();
               JSONArray floor = new JSONArray();
               floor = (JSONArray) observations.get("floor3x3");
               System.out.println(floor);*/
               //-----
               //JSONObject jobj = new JSONObject();
               //String grid = jobj.getJSONObject("floor3x3").getString("floor3x3");
               //JSONArray json = new JSONArray();
               //json.getJSONArray(0);
               //String obj = (String) json.get("floor3x3");
               //System.out.print(json);
               //System.out.print( world_state.getNumberOfObservationsSinceLastState());
               //String msg = world_state.getObservations().toString();
               //JSONObject json = new JSONObject();
               //String observation = json.getString(msg);
               //String grid[] = null;
               //for( int i = 0; i < world_state.getObservations().size(); i++ ) {
                   //System.out.print(world_state.getObservations().get(i));
               //}
           }

           /*
           if world_state.number_of_observations_since_last_state > 0:
               msg = world_state.observations[-1].text
               observations = json.loads(msg)
               grid = observations.get(u'floor3x3', 0)
               if jumping and grid[4]!=u'lava':
                   agent_host.sendCommand("jump 0")
                   jumping = False
               if grid[3]==u'lava':
                   agent_host.sendCommand("jump 1")
                   jumping = True            
           */
           
       } while( world_state.getIsMissionRunning() );
       
       System.out.println( "Mission ended" );
       //Mission has ended.
       //END
   }
   
}