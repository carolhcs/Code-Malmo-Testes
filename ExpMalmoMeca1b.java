/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package expmalmomeca1b;
import com.microsoft.msr.malmo.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author carol
 */

public class ExpMalmoMeca1b {

    static
    {
        System.loadLibrary("MalmoJava"); // attempts to load MalmoJava.dll (on Windows) or libMalmoJava.so (on Linux)
    }
    
    /**
     * @param argv the command line arguments
     */
    ////public static void main(String argv[])
    ////public static void main(String[] args) {
    public static void main(String argv[]){
        // TODO code application logic here
        AgentHost agent_host = new AgentHost(); //Criar agente
        try {
            StringVector args = new StringVector();
            //args.add("JavaExamples_run_mission");
            args.add("ExpMalmoMeca1b");
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
                
        MissionSpec my_mission = new MissionSpec();
        MissionRecordSpec my_mission_record = new MissionRecordSpec();
        my_mission.setSummary("Primeiro Experimento de Missão");
        //my_mission.timeLimitInSeconds(60);
        
        //Definindo Missão
        //Attempt to start a mission:
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
        agent_host.sendCommand("move 1");
        agent_host.sendCommand("turn -0.5");
        agent_host.sendCommand("jump 1");
        agent_host.sendCommand("pitch 1");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExpMalmoMeca1.class.getName()).log(Level.SEVERE, null, ex);
        }
        agent_host.sendCommand("attack 1");
        //Commands End

        //Loop until mission ends:
        do {
            
            try {
                Thread.sleep((long) 0.1);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExpMalmoMeca1b.class.getName()).log(Level.SEVERE, null, ex);
            }
            world_state = agent_host.getWorldState();
            for( int i = 0; i < world_state.getErrors().size(); i++ ) {
                TimestampedString error = world_state.getErrors().get(i);
                System.err.println( "Error: " + error.getText() );
            }
            
            
        } while( world_state.getIsMissionRunning() );
        System.out.println( "Mission ended" );
        //Mission has ended.
        //END
    }
    
}
