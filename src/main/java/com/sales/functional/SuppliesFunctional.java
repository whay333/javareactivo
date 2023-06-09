package com.sales.functional;


import com.sales.functional.database.Database;
import com.sales.functional.entities.Customer;
import com.sales.functional.entities.Sale;

import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


import java.util.stream.Collectors;





public class SuppliesFunctional {
    static ArrayList<Sale> sales = Database.loadDatabase();
    public static void main(String[] args) {
        loadMenu();

    }


    /** 1. Obtenga todas las ventas(Sale) que tengan como método de compra(Purchase method) 'Online'

        2. Obtenga todas las ventas(Sale) que tengan como ubicación New York y filtre también validando si las ventas fueron con cupón o sin cupón

        3. Obtenga la cantidad de ventas en las que los clientes usaron cupón

        4. Obtenga todas las ventas que fueron realizadas un año específico 'YYYY'

        5. Obtenga el número de ventas en donde el indicador de satisfacción es menor a 4.

        6. Calcule el monto total que pagó el cliente en cada venta.

        7. Obtenga todas las ventas en las que el comprador es una mujer y fue comprado en la tienda ('in store')

        8. Obtenga los productos comprados por todos los clientes segmentándolos por etiquetas(tags)

        9. Obtenga cuantos hombres usaron cupón y cuantas mujeres usaron cupón;

        10. Obtenga la venta con la compra más costosa y la venta con la compra más barata
     */

    public static void menu(){
        System.out.println("Supplies sales");
        System.out.println("1. Compras en linea");
        System.out.println("2. Compras realizadas en New York con o sin cupón");
        System.out.println("3. el numero de ventas en donde se usaron cupones y en el numero en las que no");
        System.out.println("4. Ventas realizadas en el año YYYY");
        System.out.println("5. Ventas en donde el indicador de satisfacción es menor a N");
        //TO DO:
        System.out.println("6. Monto total pagado en cada venta");
        System.out.println("7. Ventas en donde compró una mujer en la tienda(in store)");
        System.out.println("8. Agrupación de productos por etiquetas(tags)");
        System.out.println("9. Cuantos hombres y mujeres usaron cupón");
        System.out.println("10. Venta con mayor costo y menor costo");

    }

    public static void loadMenu(){
        Scanner sc = new Scanner(System.in);
        menu();
        System.out.print("Type option: ");
        String op=sc.nextLine();
        switch(op){
            case "1":
                getOnlinePurchases();
                break;
            case "2":
                System.out.print("¿quiere filtrar las ventas que usaron cupón? Y/N: ");
                getNySales(sc.nextLine());
                break;
            case "3":
                couponUsage();
                break;
            case "4":
                System.out.print("Cual es el año por el que quiere filtrar: ");
                salesByYear(sc.nextLine());
                break;
            case "5":
                System.out.print("Cual es el numero de satisfacción por que quiere filtrar (1-5): ");
                salesBySatisfaction(sc.nextLine());
                break;
            case "6":
                System.out.print("Monto total que pagó un cliente en cada venta: ");
                getTotalPayCustomer();
                break;
            case "7":
                getSailsWomen();
                break;
            case "8":
                System.out.print("Cual es el numero de satisfacción por que quiere filtrar (1-5): ");
                salesBySatisfaction(sc.nextLine());
                break;
            case "9":
                amountCoupon();
                break;
            case "10":
                maxMinSales();
                break;
            default:
                System.out.println("ERROR en el input, este metodo no ha sido creado. Intente de nuevo");
        }
    }

    //1.
    public static void getOnlinePurchases(){
        Predicate<Sale> onlinePurchased = sale -> sale.getPurchasedMethod().equals("Online");
        ArrayList<Sale> result = sales.stream().filter(onlinePurchased).collect(Collectors.toCollection(ArrayList::new));
        result.forEach(System.out::println);

    }
    //2.
    public static void getNySales(String inCoupon){
        Predicate<Sale> couponUsage = sale -> sale.getCouponUsed().equals(inCoupon.equalsIgnoreCase("Y")) && sale.getLocation().equals("New York");
        ArrayList<Sale> result = sales.stream().filter(couponUsage).collect(Collectors.toCollection(ArrayList::new));
        result.forEach(System.out::println);

    }
    //3.
    public static void couponUsage(){
        Predicate<Sale> couponUsage = Sale::getCouponUsed;
        Predicate<Sale> couponNoUsage = sale -> !sale.getCouponUsed();
        Map<String,Long> usage  = new HashMap<>(){{
            put("Usage",sales.stream().filter(couponUsage).count());
            put("Not usage",sales.stream().filter(couponNoUsage).count());
        }};

        usage.forEach((key,value)-> System.out.println(key+": "+value));

    }
    //4.
    public static void salesByYear(String inYear){
        Function<Sale,String> getYear = sale -> String.valueOf(sale.getSaleDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
        ArrayList<Sale> salesByYYYY = sales.stream().filter(sale -> getYear.apply(sale).equals(inYear)).collect(Collectors.toCollection(ArrayList::new));
        salesByYYYY.forEach(System.out::println);
    }
    //5.
    public static void salesBySatisfaction(String inSatis){
        Consumer<String> satisfaction = satis -> sales.stream().filter(sale -> sale.getCustomer().getSatisfaction().toString().equals(satis)).collect(Collectors.toCollection(ArrayList::new)).forEach(System.out::println);
        satisfaction.accept(inSatis);
    }



    //6. Calcule el monto total que pagó el cliente en cada venta.
//    public static ArrayList<Sale> getSales(ArrayList Sale) {
//        ArrayList<Sale> newList = new ArrayList<>();
//        sales.stream().map(sale -> {
//            long total_sale = sale.getItems().stream().reduce(0,Integer::sum);
//        })
//        return sales;
//    }
    public static void getTotalPayCustomer(){
        Function<Sale,String> totalPayCustomer = sale -> String.valueOf("Cliente " + sale.getCustomer() + " Monto Total ");
    }
    //7.
    public static void getSailsWomen(){
        Predicate<Sale> sailsWomen = sale -> sale.getCustomer().getGender().equalsIgnoreCase("F")
                && sale.getPurchasedMethod().equalsIgnoreCase("In store");
        ArrayList<Sale> result = sales.stream().filter(sailsWomen).collect(Collectors.toCollection(ArrayList::new));
        result.forEach(System.out::println);
    }
    //9.
    public static void amountCoupon(){
        Predicate<Sale> menCoupon = sale -> sale.getCustomer().getGender().equalsIgnoreCase("M") && sale.getCouponUsed();
        Predicate<Sale> womenCoupon = sale -> sale.getCustomer().getGender().equalsIgnoreCase("F") && sale.getCouponUsed();
        long totalmen = sales.stream().filter(menCoupon).count();
        long totalwomen = sales.stream().filter(womenCoupon).count();
        System.out.println("totalmen = " + totalmen);
        System.out.println("totalwomen = " + totalwomen);
    }

    //10.
    public static void maxMinSales(){
        Optional<Double> maxSale = sales.stream().map(Sale::getTotal).max((v1,v2) -> (int) Math.max(v1,v2));
        Optional<Double> minSale = sales.stream().map(Sale::getTotal).min((v1,v2) -> (int) Math.min(v1,v2));
        System.out.println("maxSale = " + maxSale);
        System.out.println("minSale = " + minSale);
    }
}
