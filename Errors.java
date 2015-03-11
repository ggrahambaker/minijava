class Main {
    public static void main(String[] a){
        System.out.println(0);
    }
}

class Errors {
    int[] ints;
    int n;
   //  int[] ints;  // Local var redeclaration.  Delete and retry

    // Formal param redeclaration (delete one to continue tests)
    public int pointless(int arg) {

        boolean ret = !arg;
        return arg;
    }
    // ints = new int[10];
    // int[] ints2;
    // ints2 = new int[false];
    
    // n = false;

    // Method redeclaration (no overloading in MiniJava)
    // Delete this method to continue tests
    // public int pointless(int i, int j, int k) {
    //     return 7;
    // }

    // Formal clashes with local
    public int localFormalClash(int arg) {
        boolean argz;
        return 7;
    }
}

// Shouldn't be able to have TWO classes named Errors
class ErrorsZ {
    int foo;
}