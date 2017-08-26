/* Sintactico.java */
/* Generated By:JavaCC: Do not edit this line. Sintactico.java */
package gramatica;
import ArbolAST.Nodo;
import javax.swing.JOptionPane;


public class Sintactico implements SintacticoConstants {
public static Nodo root=null;

 public Nodo retornarArbol(){
        return root;
 }
 public static void main(String args[]) throws ParseException
        {
                try{
                        Sintactico miParser = new Sintactico(System.in);
                   root = miParser .Inicio();
                    JOptionPane.showMessageDialog(null, "Parseo Correcto", "COMPI2", 1);
                }catch(ParseException e){
                        System.out.println("Error al parsear, "+e.getMessage());
                        JOptionPane.showMessageDialog(null, "Parseo INCORRECTO", "COMPI2", 0);
                }

        }

  final public Nodo Inicio() throws ParseException {Nodo raiz,sent;
raiz= new Nodo("INICIO",0,0);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CREAR:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      sent = Sentencias_Padre();
raiz.hijos.addLast(sent);
    }
    jj_consume_token(0);
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Sentencias_Padre() throws ParseException {Nodo raiz; Token v;
    jj_consume_token(CREAR);
    raiz = Sentencias_Crear();
    jj_consume_token(ptc);
{if ("" != null) return raiz;}
    jj_consume_token(USAR);
    v = jj_consume_token(IDEN);
    jj_consume_token(ptc);
raiz= new Nodo("USAR",v.beginLine,v.beginColumn,v.image);
    throw new Error("Missing return statement in function");
  }

  final public Nodo Sentencias_Crear() throws ParseException {Nodo raiz,sent; Token v;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case BASE_DATOS:{
      jj_consume_token(BASE_DATOS);
      v = jj_consume_token(IDEN);
raiz = new Nodo("BASE_DATOS",v.beginLine,v.beginColumn,v.image);
      break;
      }
    case TABLA:{
      jj_consume_token(TABLA);
      sent = Sentencias_Tabla();
raiz=sent;
      break;
      }
    case PROCEDIMIENTO:{
      jj_consume_token(PROCEDIMIENTO);
      sent = Sentencias_Procedimiento();
raiz=sent;
      break;
      }
    case OBJECTO:{
      jj_consume_token(OBJECTO);
      v = jj_consume_token(IDEN);
      jj_consume_token(apar);
raiz = new Nodo("OBJECTO",v.beginLine,v.beginColumn,v.image);
      label_2:
      while (true) {
        sent = Sentencias_Objeto();
raiz.hijos.addLast(sent);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BOOL:
        case CHAR:
        case INTEGER:
        case TEXT:
        case DOUBLE:
        case VACIO:
        case DATE:
        case DATETIME:{
          ;
          break;
          }
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
      }
      jj_consume_token(cpar);
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

  final public Nodo Sentencias_Tabla() throws ParseException {Nodo raiz,sent,atributos; Token v;
    v = jj_consume_token(IDEN);
raiz=new Nodo("TABLA",v.beginLine,v.beginColumn,v.image);
atributos=new Nodo("ATRIBUTOS",0,0);
    jj_consume_token(apar);
    sent = Atributos_Tabla();
atributos.hijos.addLast(sent);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case coma:{
        ;
        break;
        }
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      jj_consume_token(coma);
      sent = Atributos_Tabla();
atributos.hijos.addLast(sent);
    }
    jj_consume_token(cpar);
raiz.hijos.addLast(atributos);
         {if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Atributos_Tabla() throws ParseException {Nodo raiz,features,aux;String tipo; Token t;
features = new Nodo("FEATURES",0,0);
    tipo = Tipo_Var();
    t = jj_consume_token(IDEN);
raiz= new Nodo("ATRIBUTO_TABLA",t.beginLine,t.beginColumn,tipo,t.image);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PK:
      case AUTO_INCREMENT:
      case FK:
      case NOT_NULL:
      case NULO:{
        ;
        break;
        }
      default:
        jj_la1[4] = jj_gen;
        break label_4;
      }
      aux = Caracteristicas();
features.hijos.addLast(aux);
    }
raiz.hijos.addLast(features);
                {if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Caracteristicas() throws ParseException {Nodo raiz;Token v;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case FK:{
      jj_consume_token(FK);
      v = jj_consume_token(IDEN);
raiz=new Nodo("FK",v.beginLine,v.beginColumn,v.image);
      break;
      }
    case PK:{
      v = jj_consume_token(PK);
raiz=new Nodo("PK",v.beginLine,v.beginColumn,v.image);
      break;
      }
    case AUTO_INCREMENT:{
      v = jj_consume_token(AUTO_INCREMENT);
raiz=new Nodo("AUTO_INCREMENT",v.beginLine,v.beginColumn,v.image);
      break;
      }
    case NOT_NULL:{
      v = jj_consume_token(NOT_NULL);
raiz=new Nodo("NOT NULL",v.beginLine,v.beginColumn,v.image);
      break;
      }
    case NULO:{
      v = jj_consume_token(NULO);
raiz=new Nodo("NULL",v.beginLine,v.beginColumn,v.image);
{if ("" != null) return raiz;}
      break;
      }
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

    final public Nodo Sentencias_Procedimiento() throws ParseException {
        Nodo raiz, parametros, p, sentencias;
        Token t;
        parametros = new Nodo("PARAMETROS", 0, 0);
        sentencias = new Nodo("SENTENCIAS", 0, 0);
        t = jj_consume_token(IDEN);
        raiz = new Nodo("PROCEDIMIENTO", t.beginLine, t.beginColumn, t.image);
        jj_consume_token(apar);
        switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
            case BOOL:
            case CHAR:
            case INTEGER:
            case TEXT:
            case DOUBLE:
            case VACIO:
            case DATE:
            case DATETIME: {
                p = Parametros();
                parametros.hijos.addLast(p);
                label_5:
                while (true) {
                    switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
                        case coma: {
                            ;
                            break;
                        }
                        default:
                            jj_la1[6] = jj_gen;
                            break label_5;
                    }
                    jj_consume_token(coma);
                    p = Parametros();
                    parametros.hijos.addLast(p);
                }
                break;
            }
            default:
                jj_la1[7] = jj_gen;
                ;
        }
        jj_consume_token(cpar);
        jj_consume_token(alla);
        label_6:
        while (true) {
            p = Sentencias_Usql();
            sentencias.hijos.addLast(p);
            switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
                case MIENTRAS:
                case DECLARAR: {
                    ;
                    break;
                }
                default:
                    jj_la1[8] = jj_gen;
                    break label_6;
            }
        }
        jj_consume_token(clla);
        raiz.hijos.addLast(parametros);
        raiz.hijos.addLast(sentencias);
        {
            if ("" != null) {
                return raiz;
            }
        }
        throw new Error("Missing return statement in function");
    }

  final public Nodo Parametros() throws ParseException {Nodo raiz,aux;String tipo; Token t;
    tipo = Tipo_Var();
    jj_consume_token(key);
    t = jj_consume_token(IDEN);
raiz=new Nodo("PARAMETRO",t.beginLine,t.beginColumn,tipo);
                raiz.hijos.addLast(new Nodo(t.image,t.beginLine,t.beginColumn));
                {if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Sentencias_Usql() throws ParseException {Nodo sent;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case MIENTRAS:{
      sent = Ciclo_While();
      break;
      }
    case DECLARAR:{
      sent = Declaracion();
{if ("" != null) return sent;}
      break;
      }
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Nodo Ciclo_While() throws ParseException {Nodo raiz,aux;Token t;
    t = jj_consume_token(MIENTRAS);
raiz= new Nodo("WHILE",t.beginLine,t.beginColumn);
    jj_consume_token(apar);
    aux = Rel_Or();
raiz.hijos.addLast(aux);
    jj_consume_token(cpar);
    jj_consume_token(alla);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case MIENTRAS:
      case DECLARAR:{
        ;
        break;
        }
      default:
        jj_la1[10] = jj_gen;
        break label_7;
      }
      aux = Sentencias_Usql();
raiz.hijos.addLast(aux);
    }
    jj_consume_token(clla);
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Sentencias_Objeto() throws ParseException {Nodo raiz; String tipo;Token t;
    tipo = Tipo_Var();
    t = jj_consume_token(IDEN);
raiz= new Nodo("ATRIBUTO_OBJETO",t.beginLine,t.beginColumn,tipo,t.image);
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Declaracion() throws ParseException {Nodo raiz,variables,aux; String tipo;Token t;
variables=new Nodo("Lista_Variables",0,0);
    t = jj_consume_token(DECLARAR);
    aux = Variables();
variables.hijos.addLast(aux);
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case coma:{
        ;
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        break label_8;
      }
      jj_consume_token(coma);
      aux = Variables();
variables.hijos.addLast(aux);
    }
    tipo = Tipo_Var();
    jj_consume_token(ASIG);
    aux = Rel_Or();
raiz=new Nodo("DECLARAR",t.beginLine,t.beginColumn,tipo);
        raiz.hijos.addLast(variables);
        raiz.hijos.addLast(aux);
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Variables() throws ParseException {Token v;
    jj_consume_token(key);
    v = jj_consume_token(IDEN);
{if ("" != null) return new Nodo("ID",v.beginLine,v.beginLine,v.image);}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Rel_Or() throws ParseException {Nodo uno,dos,raiz;
    raiz = Rel_And();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OR:{
        ;
        break;
        }
      default:
        jj_la1[12] = jj_gen;
        break label_9;
      }
      jj_consume_token(OR);
      uno = Rel_And();
dos=raiz; raiz= new Nodo ( "OR",token.beginLine,token.beginColumn ); raiz.hijos.add(dos);raiz.hijos.add(uno);
    }
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Rel_And() throws ParseException {Nodo uno,dos,raiz;
    raiz = Rel_Not();
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AND:{
        ;
        break;
        }
      default:
        jj_la1[13] = jj_gen;
        break label_10;
      }
      jj_consume_token(AND);
      uno = Rel_Not();
dos=raiz; raiz= new Nodo ( "AND",token.beginLine,token.beginColumn ); raiz.hijos.add(dos);raiz.hijos.add(uno);
    }
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Rel_Not() throws ParseException {Nodo uno,dos,raiz;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case key:
    case NUM:
    case FALSE:
    case TRUE:
    case CARACTER:
    case CADENA:
    case DATE_EXP:
    case DATE_TIME_EXP:{
      raiz = Relacional();
      break;
      }
    case NOT:{
      jj_consume_token(NOT);
      uno = Relacional();
raiz = new Nodo("NOT",uno);
{if ("" != null) return raiz;}
      break;
      }
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Nodo Relacional() throws ParseException {Nodo uno,dos,raiz,rel;String t;
    raiz = Operacion();
    label_11:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IIG:
      case NOIG:
      case MENOR:
      case MAYOR:
      case MENORI:
      case MAYORI:{
        ;
        break;
        }
      default:
        jj_la1[15] = jj_gen;
        break label_11;
      }
      t = operadores_rel();
rel=new Nodo("OPE_REL",token.beginLine,token.beginLine, t);
      uno = Operacion();
dos = raiz; raiz=new Nodo("COND",token.beginLine,token.beginLine);
                                            raiz.hijos.add(dos);
                                            raiz.hijos.add(rel);
                                            raiz.hijos.add(uno);
    }
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public String operadores_rel() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IIG:{
      jj_consume_token(IIG);
{if ("" != null) return  token.image.toString();}
      break;
      }
    case MENORI:{
      jj_consume_token(MENORI);
{if ("" != null) return  token.image.toString();}
      break;
      }
    case MAYORI:{
      jj_consume_token(MAYORI);
{if ("" != null) return  token.image.toString();}
      break;
      }
    case MENOR:{
      jj_consume_token(MENOR);
{if ("" != null) return  token.image.toString();}
      break;
      }
    case MAYOR:{
      jj_consume_token(MAYOR);
{if ("" != null) return  token.image.toString();}
      break;
      }
    case NOIG:{
      jj_consume_token(NOIG);
{if ("" != null) return  token.image.toString();}
      break;
      }
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Nodo Operacion() throws ParseException {Nodo raiz,uno,dos;
    raiz = Multiplica();
    label_12:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case mas:
      case menos:{
        ;
        break;
        }
      default:
        jj_la1[17] = jj_gen;
        break label_12;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case mas:{
        jj_consume_token(mas);
        uno = Multiplica();
dos = raiz; raiz=new Nodo("MAS",token.beginLine,token.beginColumn); raiz.hijos.add(dos); raiz.hijos.add(uno);
        break;
        }
      case menos:{
        jj_consume_token(menos);
        uno = Multiplica();
dos = raiz; raiz=new Nodo("MENOS",token.beginLine,token.beginColumn); raiz.hijos.add(dos); raiz.hijos.add(uno);
        break;
        }
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Multiplica() throws ParseException {Nodo raiz,uno,dos;
    raiz = Potencia();
    label_13:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case div:
      case mult:{
        ;
        break;
        }
      default:
        jj_la1[19] = jj_gen;
        break label_13;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case mult:{
        jj_consume_token(mult);
        uno = Potencia();
dos = raiz; raiz=new Nodo("POR",token.beginLine,token.beginColumn); raiz.hijos.add(dos); raiz.hijos.add(uno);
        break;
        }
      case div:{
        jj_consume_token(div);
        uno = Potencia();
dos = raiz; raiz=new Nodo("DIV",token.beginLine,token.beginColumn); raiz.hijos.add(dos); raiz.hijos.add(uno);
        break;
        }
      default:
        jj_la1[20] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Potencia() throws ParseException {Nodo raiz,uno,dos;
    raiz = Expresion();
    label_14:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case potencia:{
        ;
        break;
        }
      default:
        jj_la1[21] = jj_gen;
        break label_14;
      }
      jj_consume_token(potencia);
      uno = Expresion();
dos = raiz; raiz=new Nodo("EXP",token.beginLine,token.beginColumn); raiz.hijos.add(dos); raiz.hijos.add(uno);
    }
{if ("" != null) return raiz;}
    throw new Error("Missing return statement in function");
  }

  final public Nodo Expresion() throws ParseException {Nodo r,s,v;Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case key:{
      jj_consume_token(key);
      t = jj_consume_token(IDEN);
r=new Nodo("ID",t.beginLine,t.beginColumn,t.image);
      break;
      }
    case NUM:{
      t = jj_consume_token(NUM);
r=new Nodo("NUM",t.beginLine,t.beginColumn,t.image);
      break;
      }
    case FALSE:{
      t = jj_consume_token(FALSE);
r=new Nodo("FALSE",t.beginLine,t.beginColumn,t.image);
      break;
      }
    case TRUE:{
      t = jj_consume_token(TRUE);
r=new Nodo("TRUE",t.beginLine,t.beginColumn,t.image);
      break;
      }
    case CARACTER:{
      t = jj_consume_token(CARACTER);
r=new Nodo("CHAR",t.beginLine,t.beginColumn,t.image);
      break;
      }
    case CADENA:{
      t = jj_consume_token(CADENA);
r=new Nodo("CAD",t.beginLine,t.beginColumn,t.image);
      break;
      }
    case DATE_EXP:{
      t = jj_consume_token(DATE_EXP);
r=new Nodo("DATE",t.beginLine,t.beginColumn,t.image);
      break;
      }
    case DATE_TIME_EXP:{
      t = jj_consume_token(DATE_TIME_EXP);
r=new Nodo("DATE TIME",t.beginLine,t.beginColumn,t.image);
{if ("" != null) return r;}
      break;
      }
    default:
      jj_la1[22] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public String Tipo_Var() throws ParseException {String tipo;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case BOOL:{
      jj_consume_token(BOOL);
{if ("" != null) return token.image.toString();}
      break;
      }
    case CHAR:{
      jj_consume_token(CHAR);
{if ("" != null) return token.image.toString();}
      break;
      }
    case INTEGER:{
      jj_consume_token(INTEGER);
{if ("" != null) return token.image.toString();}
      break;
      }
    case TEXT:{
      jj_consume_token(TEXT);
{if ("" != null) return token.image.toString();}
      break;
      }
    case DOUBLE:{
      jj_consume_token(DOUBLE);
{if ("" != null) return token.image.toString();}
      break;
      }
    case VACIO:{
      jj_consume_token(VACIO);
{if ("" != null) return token.image.toString();}
      break;
      }
    case DATE:{
      jj_consume_token(DATE);
{if ("" != null) return token.image.toString();}
      break;
      }
    case DATETIME:{
      jj_consume_token(DATETIME);
{if ("" != null) return token.image.toString();}
      break;
      }
    default:
      jj_la1[23] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public SintacticoTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[24];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static private int[] jj_la1_3;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
      jj_la1_init_3();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x0,0x0,0x0,0x8000000,0x0,0x0,0x8000000,0x0,0x0,0x0,0x0,0x8000000,0x4000,0x8000,0x20010000,0x3f00,0x3f00,0x300000,0x300000,0xc0000,0xc0000,0x20000,0x20000000,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x4010,0x4010,0x4010,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }
   private static void jj_la1_init_2() {
      jj_la1_2 = new int[] {0x20,0x7f800,0x1e,0x0,0x7c0,0x7c0,0x0,0x7f800,0x0,0x0,0x0,0x0,0x0,0x0,0x8d680000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x8d680000,0x7f800,};
   }
   private static void jj_la1_init_3() {
      jj_la1_3 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }

  /** Constructor with InputStream. */
  public Sintactico(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Sintactico(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new SintacticoTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Sintactico(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new SintacticoTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Sintactico(SintacticoTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(SintacticoTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
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
    boolean[] la1tokens = new boolean[97];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 24; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
          if ((jj_la1_3[i] & (1<<j)) != 0) {
            la1tokens[96+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 97; i++) {
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
