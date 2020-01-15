package nl.iprwc.controller;

import nl.iprwc.model.User;

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
    private SessionController sessionController;
    private AuthenticationController authenticationController;
    private ShoppingCartController shoppingCartController;
    private UserController userController;
    private GroupController groupController;
    private BodyLocationController bodyLocationController;
    private CategoryController categoryController;
    private CompanyController companyController;

    private SuperController() { }

    private synchronized void init()
    {
        accountController = new AccountController();
        productController = new ProductController();
        sessionController = new SessionController();
        authenticationController = new AuthenticationController();
        shoppingCartController = new ShoppingCartController();
        userController = new UserController();
        groupController = new GroupController();
        bodyLocationController = new BodyLocationController();
        categoryController = new CategoryController();
        companyController = new CompanyController();
    }

    public AccountController getAccountController() { return accountController; }
    public ShoppingCartController getShoppingCartController() { return shoppingCartController; }
    public ProductController getProductController() { return productController; }
    public SessionController getSessionController() { return sessionController; }
    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }
    public UserController getUserController() { return userController; }
    public GroupController getGroupController() { return groupController; }

    public CompanyController getCompanyController() {
        return companyController;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public BodyLocationController getBodyLocationController() {
        return bodyLocationController;
    }
}
