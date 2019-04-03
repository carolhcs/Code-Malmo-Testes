/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.microsoft.msr.malmo.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
/**
 *
 * @author carol
 */

public class ExpMalmoMeca5b {

    static
    {
        System.loadLibrary("MalmoJava"); // attempts to load MalmoJava.dll (on Windows) or libMalmoJava.so (on Linux)
    }
    
    /**
     * @param argv the command line arguments
     */
        
    public static void main(String argv[]){
        // TODO code application logic here
           
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
            args.add("ExpMalmoMeca5b");
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
            Logger.getLogger(ExpMalmoMeca5b.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        //Loop until mission starts:
        WorldState world_state;
        System.out.print( "Waiting for the mission to start" );
        do {
            System.out.print( "." );
            try {
                Thread.sleep(10);
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
        
        //Commands 
        agent_host.sendCommand("hotbar.9 1");//Press the hotbar key
        agent_host.sendCommand("hotbar.9 0");//Release hotbar key - agent should now be holding diamond_pickaxe
        agent_host.sendCommand("pitch 0.2");//Start looking downward slowly
        try {
            Thread.sleep(1);//Wait a second until we are looking in roughly the right direction
        } catch (InterruptedException ex) {
            Logger.getLogger(ExpMalmoMeca5b.class.getName()).log(Level.SEVERE, null, ex);
        }
        agent_host.sendCommand("pitch 0");//Stop tilting the camera
        agent_host.sendCommand("move 0.5");//And start running...
        agent_host.sendCommand("attack 1");//Whilst flailing our pickaxe!
        //Commands End

        boolean pular = false;
        
        //Loop until mission ends:
        do {
            
            try {
                Thread.sleep((long) 0.1);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExpMalmoMeca5b.class.getName()).log(Level.SEVERE, null, ex);
            }
            world_state = agent_host.getWorldState();
            for( int i = 0; i < world_state.getErrors().size(); i++ ) {
                TimestampedString error = world_state.getErrors().get(i);
                System.err.println( "Error: " + error.getText() );
            }

            if (world_state.getNumberOfObservationsSinceLastState() > 0){
                TimestampedStringVector observations = world_state.getObservations();
                if (observations.isEmpty())System.out.println("Observation Vazio!!");
                String obs_text = observations.get((int) (observations.size() - 1)).getText();
                JSONObject observation = new JSONObject(obs_text);
                JSONArray blocks = observation.getJSONArray("floor3x3");
                String grid[] = {"","","","","","","","",""};

                for (int i = 0; i < 9; ++i) {
                    String block = blocks.getString(i);
                    grid[i] = block;
                }

                if ("lava".equals(grid[3])){
                    agent_host.sendCommand("jump 1");
                    pular = true;
                }
                else if (!pular && (!"lava".equals(grid[4]))){
                    agent_host.sendCommand("jump 0");
                    pular = false;
                }
            }
            
        } while( world_state.getIsMissionRunning() );
        
        System.out.println( "Mission ended" );
        //Mission has ended.
        //END
    }
    
}