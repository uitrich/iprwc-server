package nl.iprwc.controller;

public class SuperController {
    private static SuperController instance;

    /**
     * Create a singleton from supercontroller, this controller has access to all other controller and can be called from anywhere.
     * @return
     */
    public static synchronized SuperController getInstance() {
        if (instance == null) {
            instance = new SuperController();
            instance.init();
        }

        return instance;
    }

    private AccountController accountController;
    private ProductController productController;

    private SuperController() { }

    private synchronized void init()
    {
        accountController = new AccountController();
        productController = new ProductController();
    }

    public AccountController getAccountController() { return accountController; }
    public ProductController getProductController() { return productController; }
}
