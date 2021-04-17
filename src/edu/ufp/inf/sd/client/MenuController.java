package edu.ufp.inf.sd.client;

public class MenuController {
    private JobShopClient client;
    public MenuController(JobShopClient client){
        this.client=client;
        System.out.println(this.client);
    }
}
