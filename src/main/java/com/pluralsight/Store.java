package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) throws IOException {
        // Initialize variables
        ArrayList<Product> inventory = new ArrayList<Product>();
        ArrayList<Product> cart = new ArrayList<Product>();
        double totalAmount = 0.0;
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("Welcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

            // Call the appropriate method based on user choice
            switch (choice) {
                case 1:
                    displayProducts(inventory, cart, scanner);
                    break;
                case 2:
                    displayCart(cart, scanner, totalAmount);
                    break;
                case 3:
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void loadInventory(String fileName, ArrayList<Product> inventory) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                    inventory.add(new Product(id, name, price, quantity));
                } else {
                    System.out.println(" " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) throws IOException {
        loadInventory("products.csv", inventory);

        System.out.println("Available Products:");
        System.out.println("ID\tName\tPrice\tQuantity");
        for (Product product : inventory) {
            System.out.println(product.getId() + "\t" + product.getName() + "\t" + product.getPrice() + "\t" + product.getQuantity());
        }

        // Prompting the user to add products to the cart
        System.out.println("Enter the ID of the Product you want to add to your cart or 'done' to finish shopping:");
        String productID = scanner.nextLine();
        while (!productID.equalsIgnoreCase("done")) {
            System.out.println("Enter the quantity:");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            Product selectedProduct = findProductById(productID, inventory);
            if (selectedProduct != null && quantity > 0 && quantity <= selectedProduct.getQuantity()) {
                Product cartProduct = new Product(selectedProduct.getId(), selectedProduct.getName(), selectedProduct.getPrice(), quantity);
                cart.add(cartProduct);
                System.out.println(quantity + " " + selectedProduct.getName() + "(s) added to cart.");
            } else {
                System.out.println("Invalid product ID or quantity. Please try again.");
            }
            System.out.println("Enter the ID of the product you want to add to the cart:");
            productID = scanner.nextLine();







        }
    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Items in the cart:");
        System.out.println("ID\tName\tPrice\tQuantity");
        for (Product product : cart) {
            System.out.println(product.getId() + "\t" + product.getName() + "\t" + product.getPrice() + "\t" + product.getQuantity());
        }

        System.out.println("Total amount: " + totalAmount);

        System.out.println("Enter the ID of the product you want to remove from your cart (or 'done' to finish):");
        String productId = scanner.nextLine();

        while (!productId.equalsIgnoreCase("done")) {
            System.out.println("Enter the quantity to remove:");
            int quantityToRemove = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Find the product in the cart
            Product productToRemove = null;
            for (Product product : cart) {
                if (product.getId().equals(productId)) {
                    productToRemove = product;
                    break;
                }
            }

            if (productToRemove != null && quantityToRemove > 0 && quantityToRemove <= productToRemove.getQuantity()) {
                // Update cart and total amount
                productToRemove.setQuantity(productToRemove.getQuantity() - quantityToRemove);
                totalAmount -= productToRemove.getPrice() * quantityToRemove;

                if (productToRemove.getQuantity() == 0) {
                    cart.remove(productToRemove);
                }
            } else {
                System.out.println("Invalid product ID or quantity. Please try again.");
            }
            System.out.println("Enter the ID of the product you want to remove from your cart (or 'done' to finish):");
            productId = scanner.nextLine();
        }
    }

    // Method to checkout (not implemented yet)
    public static void checkOut(ArrayList<Product> cart, double totalAmount, Scanner scanner) {
        System.out.println("Summary of the purchase:");
        System.out.println("Items in the cart:");
        System.out.println("ID\tName\tPrice\tQuantity");
        for (Product product : cart) {
            System.out.println(product.getId() + "\t" + product.getName() + "\t" + product.getPrice() + "\t" + product.getQuantity());
        }

        System.out.println("Total amount: " + totalAmount);

        System.out.println("Confirm purchase (yes/no):");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            System.out.println("Purchase confirmed. Total amount deducted from your account.");
            // Perform purchase logic here
        } else {
            System.out.println("Purchase canceled.");
        }
    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product product : inventory) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null; // Return null if product not found
    }
}
