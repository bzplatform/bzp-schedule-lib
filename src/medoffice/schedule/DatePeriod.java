package medoffice.schedule;

import java.util.Date;
import medoffice.entity.ProviderSchedule;

public class DatePeriod {
   Date curDate;
   int everyValue;
   Character classifierCode;
   ProviderSchedule providerSchedule;
   
   public DatePeriod() {
   }

   public DatePeriod(Date curDate) {
      this.curDate = curDate;
      this.everyValue = -1;
   }

   public Character getClassifierCode() {
      return classifierCode;
   }

   public void setClassifierCode(Character classifierCode) {
      this.classifierCode = classifierCode;
   }

   public Date getCurDate() {
      return curDate;
   }

   public void setCurDate(Date curDate) {
      this.curDate = curDate;
   }

   public int getEveryValue() {
      return everyValue;
   }

   public void setEveryValue(int everyValue) {
      this.everyValue = everyValue;
   }

   public ProviderSchedule getProviderSchedule() {
      return providerSchedule;
   }

   public void setProviderSchedule(ProviderSchedule providerSchedule) {
      this.providerSchedule = providerSchedule;
   }
}

