public class Test {
    public static void main(String[]args){
        char[] test = "test".toCharArray();

        test[2] = '!';

        System.out.println(new String(test));
    }


}
