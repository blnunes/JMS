import br.com.caelum.ConectorJms;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("use \"envia\" ou \"consome\" como parametro");
        } else {
            ConectorJms conectorJms = new ConectorJms();
            String var2 = args[0];
            byte var3 = -1;
            switch(var2.hashCode()) {
                case 96669381:
                    if (var2.equals("envia")) {
                        var3 = 0;
                    }
                    break;
                case 951510390:
                    if (var2.equals("consome")) {
                        var3 = 1;
                    }
            }

            switch(var3) {
                case 0:
                    int max = parseMax(args);
                    conectorJms.enviaMensagens(max);
                    break;
                case 1:
                    conectorJms.consomeMensagens();
            }

            conectorJms.close();
        }
    }

    private static int parseMax(String[] args) {
        return args.length >= 2 && args[1] != null ? Integer.parseInt(args[1]) : 1;
    }
}
