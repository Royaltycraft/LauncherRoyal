package fr.kintus.royalty.launcher;

public class SystemUtils {

    
    public static boolean is64bit0() { 
      String systemProp = System.getProperty("com.ibm.vm.bitmode"); 
      if (systemProp != null) { 
         return "64".equals(systemProp); 
      } 
      systemProp = System.getProperty("sun.arch.data.model"); 
      if (systemProp != null) { 
         return "64".equals(systemProp); 
      } 
      systemProp = System.getProperty("java.vm.version"); 
      return systemProp != null && systemProp.contains("_64"); 
    } 
}
