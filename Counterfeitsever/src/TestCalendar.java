
import java.util.Calendar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bala J
 */
public class TestCalendar {
    
     public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();

        System.out.println("Year: " + calendar.get(Calendar.YEAR));
        System.out.println("Month: " + calendar.get(Calendar.MONTH));
        System.out.println("Date: " + calendar.get(Calendar.DATE));

        System.out.println("Hour: " + calendar.get(Calendar.HOUR));
        System.out.println("Minute: " + calendar.get(Calendar.MINUTE));
        System.out.println("Second: " + calendar.get(Calendar.SECOND));
    }
}
