package medoffice.schedule;

import static medoffice.schedule.ScheduleUtil.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimePeriodManager {

   Date activeDate;
   Character classifierCode;
   List<TimePeriod> scheduleTimePeriodList, timePeriodList, availableTimePeriodList, goodTimePeriodList;
   int duration;

   public TimePeriodManager() {
      this.classifierCode = 'O';
      this.duration = 0;
   }

   public boolean active() {
      return (classifierCode != null && classifierCode == 'S');
   }

   public String getClassifierName() {
      if (classifierCode != null && classifierCode == 'S') {
         return "Service";
      }
      if (classifierCode != null && classifierCode == 'H') {
         return "Holiday";
      }
      if (classifierCode != null && classifierCode == 'M') {
         return "Maintenance";
      }
      if (classifierCode != null && classifierCode == 'V') {
         return "Vacation";
      }
      if (classifierCode != null && classifierCode == 'O') {
         return "Day Off";
      }
      return null;
   }

   public Date getActiveDate() {
      return activeDate;
   }

   public void setActiveDate(Date activeDate) {
      this.activeDate = activeDate;
   }

   public List<TimePeriod> getAvailableTimePeriodList() {
      if (availableTimePeriodList == null) {
         availableTimePeriodList = new ArrayList();
      }
      return availableTimePeriodList;
   }

   public void setAvailableTimePeriodList(List<TimePeriod> availableTimePeriodList) {
      this.availableTimePeriodList = availableTimePeriodList;
   }

   public Character getClassifierCode() {
      return classifierCode;
   }

   public void setClassifierCode(Character classifierCode) {
      this.classifierCode = classifierCode;
   }

   public int getDuration() {
      return duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public List<TimePeriod> getScheduleTimePeriodList() {
      if (scheduleTimePeriodList == null) {
         scheduleTimePeriodList = new ArrayList();
      }
      return scheduleTimePeriodList;
   }

   public void setScheduleTimePeriodList(List<TimePeriod> scheduleTimePeriodList) {
      this.scheduleTimePeriodList = scheduleTimePeriodList;
   }

   public List<TimePeriod> getGoodTimePeriodList() {
      if (goodTimePeriodList == null) {
         goodTimePeriodList = new ArrayList();
      }
      return goodTimePeriodList;
   }

   public void setGoodTimePeriodList(List<TimePeriod> goodTimePeriodList) {
      this.goodTimePeriodList = goodTimePeriodList;
   }

   public List<TimePeriod> getTimePeriodList() {
      if (timePeriodList == null) {
         timePeriodList = new ArrayList();
      }
      return timePeriodList;
   }

   public void setTimePeriodList(List<TimePeriod> timePeriodList) {
      this.timePeriodList = timePeriodList;
   }

   public void schrinkSlotsByBusyTime(List<TimeSlot> busySlotList) {
      int i = 0;
      while (i < timePeriodList.size()) {
         for (int j = 0; j < busySlotList.size(); j++) {
            if (busySlotList.get(j).getStartTime().compareTo(timePeriodList.get(i).getStartTime()) <= 0
                    && timePeriodList.get(i).getEndTime().compareTo(busySlotList.get(j).getEndTime()) <= 0) {
               timePeriodList.remove(i);
               i--;
               break;
            } else {
               if (busySlotList.get(j).getStartTime().compareTo(timePeriodList.get(i).getStartTime()) <= 0
                       && timePeriodList.get(i).getStartTime().before(busySlotList.get(j).getEndTime())
                       && timePeriodList.get(i).getEndTime().after(busySlotList.get(j).getEndTime())) {
                  timePeriodList.get(i).setStartTime(busySlotList.get(j).getEndTime());
               } else {
                  if (timePeriodList.get(i).getStartTime().before(busySlotList.get(j).getStartTime())) {
                     if (timePeriodList.get(i).getEndTime().equals(busySlotList.get(j).getEndTime())) {
                        timePeriodList.get(i).setEndTime(busySlotList.get(j).getStartTime());
                     } else {
                        if (timePeriodList.get(i).getEndTime().after(busySlotList.get(j).getEndTime())) {
                           Date temp = timePeriodList.get(i).getEndTime();
                           timePeriodList.get(i).setEndTime(busySlotList.get(j).getStartTime());
                           timePeriodList.add(i + 1, new TimePeriod(busySlotList.get(j).getEndTime(), temp));
                        } else if (timePeriodList.get(i).getEndTime().before(busySlotList.get(j).getEndTime())
                                && timePeriodList.get(i).getEndTime().after(busySlotList.get(j).getStartTime())) {
                           timePeriodList.get(i).setEndTime(busySlotList.get(j).getStartTime());
                        }
                     }
                  }
               }
            }
         }
         i++;
      }
      busySlotList.clear();
   }

   public void defineAvailablePeriods() {
      getAvailableTimePeriodList().addAll(getTimePeriodList());
   }

   public void defineGoodTimes(Date startCond, Date endCond, int duration) {
      Date ass, ae, timeAdd;
      for (TimePeriod timePeriod : getAvailableTimePeriodList()) {
         if (startCond.before(timePeriod.getEndTime()) && endCond.after(timePeriod.getStartTime())) {
            if (startCond.after(timePeriod.getStartTime())) {
               ass = startCond;
            } else {
               ass = timePeriod.getStartTime();
            }
            if (endCond.after(timePeriod.getEndTime())) {
               ae = endCond;
            } else {
               ae = timePeriod.getEndTime();
            }
            timeAdd = incMinute(ass, duration);
            if (timeAdd.compareTo(ae) <= 0) {
               getGoodTimePeriodList().add(new TimePeriod(ass, ae));
            }
         }
      }
   }
   
   public boolean busyTime(Date time, int duration) {
      for (TimePeriod timePeriod : availableTimePeriodList) {
         if (timePeriod.timeList(duration).contains(time)) {
            return false;
         }
      }
      return true;      
   }

}
