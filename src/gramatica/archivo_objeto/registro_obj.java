/* registro_obj.java */
/* Generated By:JavaCC: Do not edit this line. registro_obj.java */
package gramatica.archivo_objeto;
import ArbolAST.Nodo;
import javax.swing.JOptionPane;


public class registro_obj implements registro_objConstants {
public static Nodo root=null;

 public Nodo retornarArbol(){
        return root;
 }
 public static void main(String args[]) throws ParseException
        {
                try{
                        registro_obj miParser = new registro_obj(System.in);
                    root = miParser .Inicio();
                    JOptionPane.showMessageDialog(null, "Parseo Correcto", "COMPI2", 1);
                }catch(ParseException e){
                        System.out.println("Error al parsear, "+e.getMessage());
                        JOptionPane.showMessageDialog(null, "Parseo INCORRECTO", "COMPI2", 0);
                }

        }

  final public Nodo Inicio() throws ParseException {Nodo raiz,r;
raiz= new Nodo("registro_obj",0,0);
    label_1:
    while (true) {
      r = Object_U();
raiz.hijos.addLast(r);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case Obj:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(0);
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Object_U() throws ParseException {Nodo raiz,r,atrib;Token b;
atrib= new Nodo("ATRIBUTO",0,0);
    jj_consume_token(Obj);
    jj_consume_token(NOMBRE);
    b = jj_consume_token(CADENA);
    jj_consume_token(NOMBRE);
    jj_consume_token(Attr);
    label_2:
    while (true) {
      r = Atributo();
atrib.hijos.addLast(r);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INT:
      case BOOL:
      case TEXT:{
        ;
        break;
        }
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
    }
    jj_consume_token(C_Attr);
    jj_consume_token(C_Obj);
raiz = new Nodo("OBJETO",b.beginLine,b.beginColumn,b.image);
  raiz.hijos.addLast(atrib);
  {if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Atributo() throws ParseException {Nodo raiz;Token a,b;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INT:{
      a = jj_consume_token(INT);
      b = jj_consume_token(IDEN);
      jj_consume_token(C_INT);
      break;
      }
    case BOOL:{
      a = jj_consume_token(BOOL);
      b = jj_consume_token(IDEN);
      jj_consume_token(C_BOOL);
      break;
      }
    case TEXT:{
      a = jj_consume_token(TEXT);
      b = jj_consume_token(IDEN);
      jj_consume_token(C_TEXT);
raiz = new Nodo("ATRIBUTO",a.beginLine,a.beginColumn,a.image,b.image);
  {if ("" != null) return raiz;}
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public registro_objTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[3];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x40,0x7000,0x7000,};
   }

  /** Constructor with InputStream. */
  public registro_obj(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public registro_obj(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new registro_objTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public registro_obj(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new registro_objTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public registro_obj(registro_objTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(registro_objTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[26];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 3; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 26; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
