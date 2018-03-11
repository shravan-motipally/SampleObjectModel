package com.barclaycardus.conveyor;


import com.barclaycardus.conveyor.exception.NotFoundException;
import com.barclaycardus.conveyor.exception.ValidationException;
import com.barclaycardus.conveyor.vo.Bagger;
import com.barclaycardus.conveyor.vo.Conveyor;
import com.barclaycardus.conveyor.vo.DepartureSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Application
{
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static String INPUT_FILE_PATH = "input.txt";
    public static void main(String[] args)
    {
        /**
         * Each input section is started with # Section
         * example
         *
         * # Section Flights
         * A1 A2 5
         * MainEntry A1 2
         * A2 A3 2
         * A3 A1 1
         * BaggageClaim MainEntry 1
         * # Section Departure Schedule
         * BOE777 A1 JFK 09:00
         * UAE747 A3 IAH 14:30
         * # Section Bags
         * 001 MainEntry UAE747
         * 002 A2 ARRIVAL
         * 003 A3 BOE777
         */
        if (args.length != 0)
        {
            File input = new File(args[0]);
            if (input.exists() && !input.isDirectory())
                INPUT_FILE_PATH = input.getAbsolutePath();
            else {
                System.out.println(getUsage());
            }

        }

        run();
    }

    private static String getUsage()
    {
        String usage =  "*************\n" +
                "Bagger Usage\n" +
                "*************\n" +
                "conveyor <INPUT_FILE_PATH>" +
                "where <INPUT_FILE_PATH> contains a file as follows:"+
                "# Section: A weighted bi-directional graph describing the conveyor system\n" +
                "Format: <Node 1> <Node 2> <travel_time>\n" +
                "              \n" +
                "# Section: Departure list Format:\n" +
                "<flight_id> <flight_gate> <destination> <flight_time: HH:SS>\n" +
                "              \n" +
                "# Section: Bag list format:\n" +
                "<bag_number> <entry_point> <flight_id>\n";

        return usage;
    }

    private static void logException(Exception e)
    {
        logger.error(e.getClass().getName() + " caught.  Detail:\n" + e.getMessage() + "\n");
        System.out.println(getUsage());
    }

    private static void run()
    {
        Conveyor conveyor = new Conveyor();
        Bagger bagger = Bagger.getInstance();
        DepartureSchedule schedule = DepartureSchedule.getInstance();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(INPUT_FILE_PATH))));
            String line = "";
            int section = 0;
            String items[];

            inputLoop: while ((line = reader.readLine()) != null)
            {
                if (line.toLowerCase().contains("# section:") || line.toLowerCase().contains("# section")
                        || line.toLowerCase().contains("section " + section)
                        || line.toLowerCase().contains("section " + section + ":"))
                {
                    section++;
                    continue;
                }
                switch (section)
                {
                    case 0:
                        throw new ValidationException("Bad input received. Check usage");
                    case 1:
                        items = line.split(" ");
                        if (items.length != 3)
                            throw new ValidationException("Bad input received. Check usage: Map requires three parameters.");
                        conveyor.addRoute(items[0], items[1], Integer.parseInt(items[2]));
                        break;
                    case 2:
                        items = line.split(" ");
                        if (items.length != 4)
                            throw new ValidationException("Bad input received. Check usage: Departure schedule requires four parameters");
                        schedule.addFlight(items[0], items[1], items[2], items[3]);
                        break;
                    case 3:
                        items = line.split(" ");

                        if (items.length == 3)
                            bagger.addBag(items[0], items[1], items[2]);
                        else if (items.length < 3) {
                            //this is ignored.
                        }
                        else
                            throw new ValidationException("Bad input received. Check usage: Bagger requires three parameters");

                        break;
                    default:
                        break inputLoop;
                    //mistakenly input gave another # section which is ok. will break from
                }
            }
            Bagger.printOptimalRoutes(conveyor);
        }
        catch (IOException ioException)
        {
            logException(ioException);
        }
        catch (ValidationException validationException)
        {
            logException(validationException);
        }
        catch (NotFoundException notFoundException)
        {
            logException(notFoundException);
        }
        finally
        {
            try{
                if (reader != null)
                    reader.close();
            }catch (IOException io){}
        }
    }
}
