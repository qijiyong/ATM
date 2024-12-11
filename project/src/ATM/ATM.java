package ATM;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {

    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account loginAcc;//登陆后的用户
    public void start(){//系统
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("=======欢迎进入ATM系统======");
            System.out.println("请输入你要执行的操作命令");
            System.out.println("1.用户登陆");
            System.out.println("2.用户开户");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    logIn();
                break;
                case 2:
                    createAccount();
                break;
                default:
                    System.out.println("您输入的命令有误~~~~~");
            }
        }
    }
    private void createAccount(){//创建账户
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入你要创建的账户名");
        Account acc = new Account();
        String name = sc.next();
        acc.setUserName(name);

        while (true) {
            System.out.println("请输入您的性别");
            char sex = sc.next().charAt(0);//拿到用户输入的变量后取第一个字
            if(sex == '男' || sex == '女'){
                acc.setSex(sex);
                break;
            }else{
                System.out.println("您输入的性别有误~~~~~");
            }
        }
        while (true) {
            System.out.println("请输入您的密码");
            String passWord = sc.next();
            System.out.println("请输入你的确认密码");
            String passWord2 = sc.next();
            if(passWord.equals(passWord2)){
                acc.setPassWord(passWord);
                break;
            }else{
                System.out.println("您输入的2次密码不一样");
            }
        }

        System.out.println("请输入您的最大体现额度");
        double limit = sc.nextDouble();
        acc.setLimit(limit);
        //生成卡号用一个额外的方法，注意不能和之前生成的重复
        String newCardId = createCardId();
        acc.setCardId(newCardId);
        accounts.add(acc);
        System.out.println("恭喜" + acc.getUserName() +"开户成功，您的卡号是：" + acc.getCardId());

    }
    private String createCardId(){//生成随机的卡号，不能与之前的卡号重复
        Random r = new Random();
        while (true) {
            String cardId = "";
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                cardId += data;
            }
            Account acc = getAccountCardId(cardId);//接住
            if (acc == null) {
                return cardId;//如果没有，则退出方法打破while循环，否则继续生成
            }
        }
    }
    private Account getAccountCardId(String cardId){//遍历之前的账户卡号
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if(acc.getCardId().equals(cardId)){//如果有重复的，返回变量
                return acc;
            }
        }
        return null;//如果没有，返回null
    }
    private void logIn(){
        Scanner sc = new Scanner(System.in);
        System.out.println("==进入登陆系统==");
        if(accounts.size() == 0){
            System.out.println("当前没有账户");
            return;//退出方法
        }
        while (true) {
            System.out.println("请输入您的卡号");
            String cardId = sc.next();
            Account acc = getAccountCardId(cardId);//拿到这个id，就获得了整个变量
            if (acc == null) {
                System.out.println("您输入的卡号不存在，请确认~~");
            }
            else{
                while (true) {
                    System.out.println("请输入登陆密码");
                    String passWord = sc.next();
                    //判断密码是否正确
                    if(acc.getPassWord().equals(passWord)){
                        //ture的话就正确了
                        loginAcc = acc;
                        System.out.println("登陆成功，欢迎您" + acc.getUserName() + "您的卡号是：" + acc.getCardId());
                        //另外弄个方法用来操作登陆成功的系统
                        afterLogIn();
                        return;
                    }else {
                        System.out.println("您输入的密码有误");
                    }
                }
            }
        }
    }
    private void afterLogIn(){//登陆成功的操作
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(loginAcc.getUserName() + ",您可以办理以下业务");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出");
            System.out.println("7.注销账号");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    //查询
                    showAccount();
                break;
                case 2:
                    //存款
                    saveAccount();
                break;
                case 3:
                    //取款
                    withdrawAccount();
                break;
                case 4:
                    //转账
                    transfer();
                break;
                case 5:
                    //改密码
                    changPassWord();
                break;
                case 6:
                    //退出
                    System.out.println(loginAcc.getUserName() + "您退出系统成功");
                    return;
                case 7:
                    //注销
                    if (closeAccount()){
                        System.out.println("销户成功");
                    }else{
                        System.out.println("账户保留");
                    }
                break;
            }
        }
    }
    private void showAccount(){//查询账户
        System.out.println("户主：" + loginAcc.getUserName());
        System.out.println("卡号：" + loginAcc.getCardId());
        System.out.println("性别：" + loginAcc.getSex());
        System.out.println("余额：" + loginAcc.getMoney());
        System.out.println("每次取款额度" + loginAcc.getLimit());
    }

    private void saveAccount(){//存钱
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入您要存的金额");
        double money = sc.nextDouble();
        loginAcc.setMoney(loginAcc.getMoney() + money);
        System.out.println("您现有存款" + loginAcc.getMoney());
    }

    private void withdrawAccount(){//取款
        if(loginAcc.getMoney() <= 100){
            System.out.println("您的余额不足100元，不能取款");
            return;
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入您要取款的金额");
            double money = sc.nextDouble();
            if(money > loginAcc.getLimit()){
                System.out.println("单次取款超出每次取款限额，您每次最多可取" + loginAcc.getLimit() + "元");
            }
            if(loginAcc.getMoney() >= money){//如果余额够了
                loginAcc.setMoney((loginAcc.getMoney() - money));
                System.out.println("您取款" + money + "元成功，现有余额" + loginAcc.getMoney());
                break;
            }else {
                System.out.println("取款失败，您现有余额" + loginAcc.getMoney() + "，请您确认");
            }
        }
    }
    private void transfer(){//转账
        if(accounts.size() <= 1){
            System.out.println("当前账户不足两个，不能转账");
            return;
        }
        if(loginAcc.getMoney() == 0){
            System.out.println("您自己都没钱，就别转了~~~~~");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入您要转帐的卡号");
        String cardId = sc.next();
        Account acc = getAccountCardId(cardId);
        if (acc == null){
            System.out.println("您输入的卡号不存在，请确认");
        }else {
            System.out.println("请输入您要转账的金额");
            double money = sc.nextDouble();
            if(money > loginAcc.getMoney()){
                System.out.println("要转的余额不足，请确认");
            }else{
                loginAcc.setMoney(loginAcc.getMoney() - money);
                acc.setMoney(acc.getMoney() + money);
                System.out.println("转账成功，您现有余额" + loginAcc.getMoney());
            }
        }
    }
    private void changPassWord(){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入您原来的密码");
        String passWord = sc.next();
        if(loginAcc.getPassWord().equals(passWord)){
            System.out.println("请输入您的新密码");
            String newPassWord = sc.next();
            if (newPassWord.equals(loginAcc.getPassWord())){
                System.out.println("新密码与原密码重复，请确认");
            }else{
            loginAcc.setPassWord(newPassWord);
            System.out.println("修改成功！");
            }
        }else{
            System.out.println("原密码错误，请确认");
        }
    }
    private boolean closeAccount() {//销户
        Scanner sc = new Scanner(System.in);
            System.out.println("您确定注销您的账户吗？是的话请输入y，不是的话请输入n");
            String command = sc.next();
            switch (command){
                case "y":
                    if (loginAcc.getMoney() == 0) {
                        System.out.println("您的账户里还有余额，不能注销~~~~");
                        return false;
                    }else {
                        accounts.remove(loginAcc);
                        System.out.println("销户成功");
                        return true;

                    }
                default:
                    System.out.println("好的，您的账户保留！！！！");
                    return false;
            }
    }
}

