import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Functionality {

    public static int id_for_all_member = 1;

    public static String[] col_name = {"Type","ID","Name","Address","Salary","Bonus","Hour","Rate","Pay"};
    public static String[] menu_list = {"1. Insert Employee","2. Update Employee","3. Display Employee","4. Remove Employee","5. Exit"};
    public static String[] chooseStaffType = {"1. Volunteer","2. Hourly Employee","3. Salaries Employee","4. Back"};

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
            staffMembersList.forEach(person-> {
                memberType.forEach(member -> {
                    if (person.getId()== member.getId()){
                       tablePerson(t,person,numberStyle,member);
                    }
                });
            });
            System.out.println(t.render());
            new Scanner(System.in).next();
            return;
        }

        System.out.println(t.render());

    }

    public static void tablePerson(Table t,StaffMembers staffMembers,CellStyle cellStyle,StaffType staffType){
        t.addCell("\u001B[33m" + staffType.getType() + "\u001B[0m",cellStyle);
        t.addCell(String.valueOf(staffMembers.getId()),cellStyle);
        t.addCell(staffMembers.getName(),cellStyle);
        t.addCell(staffMembers.getAddress(),cellStyle);
        t.addCell(String.valueOf(staffMembers instanceof Volunteer ? ((Volunteer) staffMembers).getSalary()
                        : staffMembers instanceof HourlySalaryEmployee ? "\u001B[32m" + "----" + "\u001B[0m"
                        : staffMembers instanceof SalariedEmployee ? ((SalariedEmployee) staffMembers).getSalary()
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
        t.addCell(String.valueOf(staffMembers instanceof Volunteer ? staffMembers.pay()
                        : staffMembers instanceof HourlySalaryEmployee ? staffMembers.pay()
                        : staffMembers instanceof SalariedEmployee ? (staffMembers).pay()
                        : "")
                ,cellStyle);

    }

    public static void inputPersonData(List<StaffMembers> staffMembersList,List<StaffType> staffTypes,int id,int options){
        String type = options == 1 ? "Volunteer" : options == 2 ? "Hourly Employee" : options == 3 ? "Salaries Employee" : "";
        String name,address,salary = null,bonus = null,hour = null,rate = null;
        System.out.println(type + " ID : " + id);
        System.out.print("Enter Name : ");
        name = new Scanner(System.in).nextLine();
        System.out.print("Enter Address : ");
        address = new Scanner(System.in).nextLine();
        if (options!=2) {
            System.out.print("Enter Salary : ");
            salary = new Scanner(System.in).nextLine();
        }
        if (options == 3) {
            System.out.print("Enter Bonus : ");
            bonus = new Scanner(System.in).nextLine();
        }

        if (options == 2) {
            System.out.print("Enter Hour : ");
            hour = new Scanner(System.in).nextLine();
        }

        if (options == 2) {
            System.out.print("Enter Rate : ");
            rate = new Scanner(System.in).nextLine();
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
        }

        createTable(1,new String[]{"Added new "+ type},staffMembersList,staffTypes,"");

    }

    public static void chooseStaffToInput(List<StaffMembers> staffMembers,List<StaffType> staffTypes,int id){
        createTable(4,chooseStaffType,staffMembers,staffTypes,"");
        System.out.print("=> Enter Type Number : ");
        String option = new Scanner(System.in).next();
        inputPersonData(staffMembers,staffTypes,id,Integer.parseInt(option));
    }
    public static boolean isDataValidate(String validate_word,String regex){
        return Pattern.matches(validate_word,regex);

    }

    public static void startProgram(List<StaffMembers> staffMembers,List<StaffType> staffTypes){
        do {
            menuBox(1,menu_list,staffMembers,staffTypes);
            String opt;
            do {
                System.out.print("=> Choose an Option : ");
                opt = new Scanner(System.in).next();
                // waiting for validate with regex or exception
                if (true){
                    break;
                }
            }while (true);

            switch (opt){
                case "1":
                    chooseStaffToInput(staffMembers,staffTypes,Functionality.id_for_all_member);
                    do {
                        System.out.print("Do you want to add more ? [y/n] : ");
                        String answer = new Scanner(System.in).next();
                        if (answer.equalsIgnoreCase("y")){
                            chooseStaffToInput(staffMembers,staffTypes,Functionality.id_for_all_member);
                        }else
                            break;
                    }while (true);

                case "2":
                    break;
                case "3":
                    createTable(9,col_name,staffMembers,staffTypes,"for staff");
                    break;
                case "4":
                    removeStaff(staffMembers,staffTypes);
                    new Scanner(System.in).next();
                    break;
                case "5":
                    String opt_exit;
                    System.out.print("Do you want to continue or exit ? [y/n] : ");
                    opt_exit = new Scanner(System.in).next();
                    if (opt_exit.equalsIgnoreCase("y")){
                        createTable(1,new String[]{"Good Bye!!"},staffMembers,staffTypes,"");
                        return;
                    }
                    break;
            }


        }while (true);
    }

    public static void removeStaff(List<StaffMembers> staffMembers,List<StaffType> staffTypes){
        System.out.print("=> Enter ID to delete : ");
        String id = new Scanner(System.in).next();
        staffMembers.forEach(staff -> {
            staffTypes.forEach(staff_type -> {
                if (Integer.parseInt(id) == staff.getId() && (staff.getId() == staff_type.getId())){
                    CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
                    Table t = new Table(9, BorderStyle.UNICODE_ROUND_BOX,
                            ShownBorders.ALL);
                    t.setColumnWidth(0, 25, 25);
                    t.setColumnWidth(1, 25, 25);
                    for (String header : col_name){
                        t.addCell(header,numberStyle);
                    }
                    tablePerson(t,staff,numberStyle,staff_type);
                    System.out.println(t.render());
                    System.out.print("Do you want to delete or not ? [y/n] :");
                    String answer = new Scanner(System.in).next();
                    if (answer.equalsIgnoreCase("y")){
                        staffMembers.remove(staff);
                        staffTypes.remove(staff_type);
                        createTable(1,new String[]{"Deleted Successfully "},staffMembers,staffTypes,"");
                        return;
                    }else {
                        createTable(1,new String[]{"Cancel Delete"},staffMembers,staffTypes,"");
                        return;
                    }
                }else {
                    createTable(1,new String[]{"ID Not Found !! "},staffMembers,staffTypes,"");
                    return;
                }


                    }

            );
        });

    }

}
