import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Functionality {

    public static int id_for_all_member = 1;

    public static String[] col_name = {"Type","ID","Name","Address","Salary","Bonus","Hour","Rate","Pay"};
    public static String[] menu_list = {"1. Insert Employee","2. Update Employee","3. Display Employee","4. Remove Employee","5. Exit"};
    public static String[] chooseStaffType = {"1. Volunteer","2. Hourly Employee","3. Salaries Employee","4. Back"};
    public static String[] updateVolunteer = {"1. Name","2. Address","3. Salary","0. Cancel"};
    public static String[] updateHourlyEmp = {"1. Name","2. Address","3. Hour","4. Rate","0. Cancel"};
    public static String[] updateSalaryEmp = {"1. Name","2. Address","3. Salary","4. Bonus","0. Cancel"};

    public static void menuBox(int numberCols,String[] menu,List<StaffMembers> staffMembers,List<StaffType> staffTypes){
        createTable(numberCols,menu,staffMembers,staffTypes,"");
    }

    public  static void createTable(int numberOfCol,String[] header_title,List<StaffMembers> staffMembersList,List<StaffType> memberType,String useForWhat){
        CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table t = new Table(numberOfCol, BorderStyle.UNICODE_ROUND_BOX,
                ShownBorders.ALL);
        for (int i = 0; i < numberOfCol; i++){
            t.setColumnWidth(i, 25, 25);
        }
        for (String s : header_title) {
            t.addCell("\u001B[32m" + s + "\u001B[0m", numberStyle);
        }
        if (!useForWhat.isEmpty()){
            if (staffMembersList.isEmpty()){
                createTable(1,new String[]{"No Information right now"},staffMembersList,memberType,"");
                new Scanner(System.in).next();
                return;
            }
            staffMembersList.forEach(person-> {
                memberType.forEach(member -> {
                    if (person.getId()== member.getId()){
                       tablePerson(t,person,numberStyle,member);
                    }
                });
            });
            System.out.println(t.render());
            return;
        }
        System.out.println(t.render());

    }

    public static void tablePerson(Table t,StaffMembers staffMembers,CellStyle cellStyle,StaffType staffType){
        t.addCell("\u001B[33m" + staffType.getType() + "\u001B[0m",cellStyle);
        t.addCell(String.valueOf(staffMembers.getId()),cellStyle);
        t.addCell(staffMembers.getName(),cellStyle);
        t.addCell(staffMembers.getAddress(),cellStyle);
        t.addCell(String.valueOf(staffMembers instanceof Volunteer ?"$ "+((Volunteer) staffMembers).getSalary()
                        : staffMembers instanceof HourlySalaryEmployee ? "\u001B[32m" + "----" + "\u001B[0m"
                        : staffMembers instanceof SalariedEmployee ?"$ "+((SalariedEmployee) staffMembers).getSalary()
                        : "")
                ,cellStyle);
        t.addCell(String.valueOf(staffMembers instanceof Volunteer ? "\u001B[32m" + "----" + "\u001B[0m"
                        : staffMembers instanceof HourlySalaryEmployee ? "\u001B[32m" + "----" + "\u001B[0m"
                        : staffMembers instanceof SalariedEmployee ? ((SalariedEmployee) staffMembers).getBonus()
                        : "\u001B[32m" + "----" + "\u001B[0m")
                ,cellStyle);
        t.addCell(String.valueOf(staffMembers instanceof Volunteer ? "\u001B[32m" + "----" + "\u001B[0m"
                        : staffMembers instanceof HourlySalaryEmployee ? ((HourlySalaryEmployee) staffMembers).getHourWorked()
                        : staffMembers instanceof SalariedEmployee ? "\u001B[32m" + "----" + "\u001B[0m"
                        : "")
                ,cellStyle);
        t.addCell(String.valueOf(staffMembers instanceof Volunteer ? "\u001B[32m" + "----" + "\u001B[0m"
                        : staffMembers instanceof HourlySalaryEmployee ? ((HourlySalaryEmployee) staffMembers).getRate()
                        : staffMembers instanceof SalariedEmployee ? "\u001B[32m" + "----" + "\u001B[0m"
                        : "")
                ,cellStyle);
        t.addCell(String.valueOf(staffMembers instanceof Volunteer ?"$ "+staffMembers.pay()
                        : staffMembers instanceof HourlySalaryEmployee ?"$ "+staffMembers.pay()
                        : staffMembers instanceof SalariedEmployee ?"$ "+(staffMembers).pay()
                        : "")
                ,cellStyle);

    }

    public static void inputPersonData(List<StaffMembers> staffMembersList,List<StaffType> staffTypes,int id,int options){
        String type = options == 1 ? "Volunteer" : options == 2 ? "Hourly Employee" : options == 3 ? "Salaries Employee" : "";
        String name=null,address,salary = null,bonus = null,hour = null,rate = null;
        createTable(1,new String[]{type + " ID : " + id_for_all_member},staffMembersList,staffTypes,"");
        name = doDataValidate("[a-zA-Z]+\\s*[a-zA-Z]+","Enter Name : ","Invalid Name",staffMembersList,staffTypes);
        address = doDataValidate("[a-zA-Z]+\\s*[a-zA-Z]+","Enter Address : ","Invalid Address",staffMembersList,staffTypes);
        if (options!=2) {
            salary = doDataValidate("[0-9.0-9]+","Enter Salary : ","Invalid Salary",staffMembersList,staffTypes);
        }
        if (options == 3) {
            bonus = doDataValidate("[0-9.0-9]+","Enter Bonus : ","Invalid Bonus",staffMembersList,staffTypes);

        }

        if (options == 2) {
            hour = doDataValidate("[0-9.0-9]+","Enter Hour : ","Invalid Hour",staffMembersList,staffTypes);
        }

        if (options == 2) {
            rate = doDataValidate("[0-9.0-9]+","Enter Rate : ","Invalid Rate",staffMembersList,staffTypes);
        }

        switch (options){
            case 1 :
                staffMembersList.add(new Volunteer(id_for_all_member,name,address,Double.parseDouble(salary)));
                staffTypes.add(new StaffType(id,"Volunteer"));
                id_for_all_member++;
                break;
            case 2 :
                staffMembersList.add(new HourlySalaryEmployee(id_for_all_member,name,address,Integer.parseInt(hour),Double.parseDouble(rate)));
                staffTypes.add(new StaffType(id,"Hourly Salary Employee"));
                id_for_all_member++;
                break;
            case 3 :
                staffMembersList.add(new SalariedEmployee(id_for_all_member,name,address,Double.parseDouble(salary),Double.parseDouble(bonus)));
                staffTypes.add(new StaffType(id,"Salaries Employee"));
                id_for_all_member++;
                break;
            default:
                break;

        }

        createTable(1,new String[]{"Added new "+ type},staffMembersList,staffTypes,"");

    }

    public static void chooseStaffToInput(List<StaffMembers> staffMembers,List<StaffType> staffTypes,int id){
        createTable(4,chooseStaffType,staffMembers,staffTypes,"");
        System.out.print("=> Enter Type Number : ");
        String option = new Scanner(System.in).next();
        try{
            if (option.equalsIgnoreCase("4") || Integer.parseInt(option)>4){
                createTable(1,new String[]{"Invalid Choices"},staffMembers,staffTypes,"");
                return;
            }
        }catch (NumberFormatException e){
            createTable(1,new String[]{"Invalid Choices"},staffMembers,staffTypes,"");
            return;
        }
        inputPersonData(staffMembers,staffTypes,id,Integer.parseInt(option));
    }
    public static String doDataValidate(String regex, String inputWhat, String errorWhat, List<StaffMembers> staffMembers, List<StaffType> staffTypes){
        do {
            System.out.print(inputWhat);
            String validate_what = new Scanner(System.in).nextLine();
            if ((!Pattern.matches(regex, validate_what))) {
                createTable(1, new String[]{errorWhat}, staffMembers, staffTypes, "");
                continue;
            }
            return validate_what;
        }while (true);
    }

    public static void startProgram(List<StaffMembers> staffMembers,List<StaffType> staffTypes){
        do {
            menuBox(1,menu_list,staffMembers,staffTypes);
            String opt = doDataValidate("[0-9.0-9]+","=> Choose an Option : ","Invalid Option",staffMembers,staffTypes);
            switch (opt){
                case "1":
                    chooseStaffToInput(staffMembers,staffTypes,Functionality.id_for_all_member);
                    new Scanner(System.in).next();
                    break;
                case "2":
                    updateStaff(staffMembers,staffTypes);
                    new Scanner(System.in).next();
                    break;
                case "3":
                    createTable(9,col_name,staffMembers,staffTypes,"for staff");
                    new Scanner(System.in).next();
                    break;
                case "4":
                    removeStaff(staffMembers,staffTypes);
                    new Scanner(System.in).next();
                    break;
                case "5":
                    String opt_exit = doDataValidate("[0-9.0-9]+","=> Choose a correct answer Y or N : ","Invalid Column",staffMembers,staffTypes);
                    if (opt_exit.equalsIgnoreCase("y")){
                        createTable(1,new String[]{"Good Bye!!"},staffMembers,staffTypes,"");
                        return;
                    }
                    createTable(1,new String[]{"Continue Program"},staffMembers,staffTypes,"");
                    new Scanner(System.in).next();
                    break;
                default:
                    createTable(1,new String[]{"Invalid Choice"},staffMembers,staffTypes,"");
                    new Scanner(System.in).next();
            }
        }while (true);
    }

    public static void removeStaff(List<StaffMembers> staffMembers,List<StaffType> staffTypes){
        if (staffMembers.isEmpty()){
            createTable(1, new String[]{"No Data right now !!"},staffMembers,staffTypes,"person");
            return;
        }
        System.out.print("=> Enter ID to delete : ");
        String id = new Scanner(System.in).next();
        List<StaffMembers> member = staffMembers.stream().filter(p->p.getId()==Integer.parseInt(id)).toList();
        List<StaffType> type = staffTypes.stream().filter(p->p.getId()==Integer.parseInt(id)).toList();
        try{
            staffMembers.forEach(staff->{
                staffTypes.forEach(staff_type->{
                    if (staff.getId()==Integer.parseInt(id) && staff_type.getId() == staff.getId()){
                        createTable(9, col_name,member,type,"person");
                        System.out.print("=> Do you want to delete this staff ? [y/n] : ");
                        String answer = new Scanner(System.in).nextLine();
                        if (answer.equalsIgnoreCase("y")){
                            member.forEach(staffMembers::remove);
                            type.forEach(staffMembers::remove);
                            createTable(1,new String[]{"Deleted Staff !!"},staffMembers,staffTypes,"");
                        }else {
                            createTable(1,new String[]{"Canceled !!"},staffMembers,staffTypes,"");
                        }
                    }
                });
            });
        }catch (ConcurrentModificationException e){
        }
    }

    public static void updateStaff(List<StaffMembers> staffMembers,List<StaffType> staffTypes){
        String id = doDataValidate("[0-9]+","Enter ID to update : ","Invalid ID",staffMembers,staffTypes);
        List<StaffMembers> member = staffMembers.stream().filter(p->p.getId()==Integer.parseInt(id)).toList();
        List<StaffType> type = staffTypes.stream().filter(p->p.getId()==Integer.parseInt(id)).toList();
        try {
            staffMembers.forEach(staff -> {
                staffTypes.forEach(staff_type -> {
                    if (Integer.parseInt(id) == staff.getId() && staff.getId() == staff_type.getId()) {
                        createTable(9, col_name, member, type, "for staff");
                        updateInput(staffMembers,staffTypes,staff,staff_type);
                        createTable(9, col_name, member, type, "for staff");
                    }else {
                        createTable(1, new String[]{"ID was not found!!"}, staffMembers, staffTypes, "");
                    }
                });
            });
        }catch (ConcurrentModificationException e){

        }
    }

    public static void updateInput(List<StaffMembers> staffMembers,List<StaffType> staffTypes,StaffMembers members,StaffType staffType){
        do {
            createTable(members instanceof Volunteer ? 4
                            : members instanceof HourlySalaryEmployee ? 5
                            : members instanceof SalariedEmployee ? 5 :0,
                    members instanceof Volunteer ? updateVolunteer
                            : members instanceof HourlySalaryEmployee ? updateHourlyEmp
                            : members instanceof SalariedEmployee ? updateSalaryEmp
                            : updateVolunteer,staffMembers,staffTypes,""
            );
            System.out.print("=> Choose one column to update : ");
            String option = doDataValidate("[0-9.0-9]+","=> Choose one column to update : ","Invalid Column",staffMembers,staffTypes);
            switch (option) {
                case "0":
                    createTable(1, new String[]{"Update Exited!!"}, staffMembers, staffTypes, "");
                    return;
                case "1":
                    System.out.print("=> Change Name to : ");
                    String newName = doDataValidate("[a-zA-Z]+\\s*[a-zA-Z]+","Enter New Name : ","Invalid Name",staffMembers,staffTypes);
                    members.setName(newName);
                    createTable(1, new String[]{"Name has been updated !!"}, staffMembers, staffTypes, "");
                    break;
                case "2":
                    System.out.print("=> Change Address to : ");
                    String newAddress = doDataValidate("[a-zA-Z]+\\s*[a-zA-Z]+","Enter New Address : ","Invalid Address",staffMembers,staffTypes);
                    members.setAddress(newAddress);
                    createTable(1, new String[]{"Address has been updated !!"}, staffMembers, staffTypes, "");
                    break;
                case "3":
                    if (members instanceof HourlySalaryEmployee) {
                        System.out.print("=> Change hour to : ");
                        String newHour = doDataValidate("[0-9.0-9]+","Enter New Hour : ","Invalid Hour",staffMembers,staffTypes);
                        ((HourlySalaryEmployee) members).setHourWorked(Integer.parseInt(newHour));
                        createTable(1, new String[]{"Hour has been updated !!"}, staffMembers, staffTypes, "");
                    } else if (members instanceof Volunteer || members instanceof SalariedEmployee) {
                        System.out.print("=> Change salary to : ");
                        String newSalary = doDataValidate("[0-9.0-9]+","Enter New Salary : ","Invalid Salary",staffMembers,staffTypes);
                        if (members instanceof Volunteer) {
                            ((Volunteer) members).setSalary(Double.parseDouble(newSalary));
                            createTable(1, new String[]{"Salary has been updated !!"}, staffMembers, staffTypes, "");
                        } else {
                            ((SalariedEmployee) members).setSalary(Double.parseDouble(newSalary));
                            createTable(1, new String[]{"Salary has been updated !!"}, staffMembers, staffTypes, "");
                        }
                    }
                    break;
                case "4":
                    if (members instanceof SalariedEmployee) {
                        System.out.print("=> Change Bonus to : ");
                        String newBonus = doDataValidate("[0-9.0-9]+","Enter New Bonus : ","Invalid Bonus",staffMembers,staffTypes);
                        ((SalariedEmployee) members).setBonus(Double.parseDouble(newBonus));
                        createTable(1, new String[]{"Bonus has been updated !!"}, staffMembers, staffTypes, "");
                    } else {
                        System.out.print("=> Change Rate to : ");
                        String newRate = doDataValidate("[0-9.0-9]+","Enter New Rate : ","Invalid Rate",staffMembers,staffTypes);
                        ((HourlySalaryEmployee) members).setRate(Double.parseDouble(newRate));
                        createTable(1, new String[]{"Rate has been updated !!"}, staffMembers, staffTypes, "");
                    }
                    break;
                default:
                    createTable(1, new String[]{"Please Choose the correct option !!"}, staffMembers, staffTypes, "");
            }
        }while (true);

    }

}
