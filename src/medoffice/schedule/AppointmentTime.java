package medoffice.schedule;

import java.util.Date;

public class AppointmentTime {

   private Date date;
   private Date time;
   private Short duration;

   public AppointmentTime(Date date, Date time, Short duration) {
      this.date = date;
      this.time = time;
      this.duration = duration;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public Date getTime() {
      return time;
   }

   public void setTime(Date time) {
      this.time = time;
   }

   public Short getDuration() {
      return duration;
   }

   public void setDuration(Short duration) {
      this.duration = duration;
   }
   
   
}
