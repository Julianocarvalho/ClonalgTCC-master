package clonalgtcc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author joao
 */
public class Clonalg {

    // Utilitários
    private static final Random r = new Random();
    private static DecimalFormat df = new DecimalFormat("#.##");

    // Variavéis Sistema
    private static ArrayList<Anticorpo> populacao = new ArrayList<>();
    private ArrayList<Anticorpo> populacaof = new ArrayList<>();
    private ArrayList<Particao> particoes = new ArrayList<>();
    ArrayList<Anticorpo> tests = new ArrayList<>();
    static Leitor leitor = new Leitor();
    static QuickSort quick = new QuickSort();
    private static ArrayList<Antigeno> antigenos = leitor.leAntigenos();
    private static ArrayList<Ensemble> ensembles = new ArrayList<>();

    // Configurações.
    private static final int numExecu = 90; // número de testes
    private static final int numExecuEnsemble = 9; // rodadas de ensembles. multiplos de 9
    private static double limiar = 0.3; // número de anticorpos selecionados para serem clonados
    private static final int numClo = 5;//6 // cada selecionado possuirá este número de clones ou menos
    private static final double numSel = 0.5; // número de clones selecionados para entra na população 50%.
    private static int numGeracoes = 200;
    private static final double erroQuadratico = 0.001; // diferença necessária para parada
    private static final int tamanhoBase = 2; // dimensões da base
    private static boolean graficosG = false; // graficos de cada geração
    private static boolean graficosF = false; // graficos para cada final de execução

    /**
     * Gera uma várias populações iniciais de anticorpos aleatórios no intervalo
     * (0,1].
     *
     * @param numExec número de execuções..
     */
    public ArrayList<Anticorpo> geraPop(int numExec) {
        ArrayList<Anticorpo> iniciais = new ArrayList<>();
        for (int i = 0; i < numExec; i++) {
            Anticorpo ant = new Anticorpo(tamanhoBase);
            for (int v = 0; v < tamanhoBase; v++) {
                ant.getVars().set(v, (double) r.nextFloat());
            }

            ant.setAfinidade(-1); // afinidade negativa quando iniciado.
            iniciais.add(ant);
        }
        return iniciais;
    }

    // ARRAY LIST COM 10 ANTICORPOS INICIAS
    public ArrayList<Anticorpo> geraPopC() {
        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<Anticorpo> anticorpos = new ArrayList<>();

        String nome = "iniciais2.txt"; //17 variaveis
        try {
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            while (linha != null) { // adicionando todas linhas do arquivo no array linhas.
                linhas.add(linha);
                linha = lerArq.readLine();
            }
            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        // Separa as variáveis e cria anticorpos
        for (String linha : linhas) {
            String[] separa = linha.split(" ");
            ArrayList<Double> vars = new ArrayList<>();
            for (int i = 0; i < tamanhoBase; i++) {
                vars.add(Double.parseDouble(separa[i]));
            }
            Anticorpo ant = new Anticorpo(vars, null, -1);
            anticorpos.add(ant);
//            tests.add(ant);
        }
        return anticorpos;
    }

    public static double distanciaEuclidiana(Anticorpo anticorpo, Antigeno antigeno) {
        double soma = 0;

        for (int v = 0; v < tamanhoBase; v++) {
            soma += Math.pow((antigeno.getVars().get(v) - anticorpo.getVars().get(v)), 2);
        }

        return Math.sqrt(soma);
    }

    /**
     * Para cada antigeno selecionado calcular o anticorpo de maior afinidade e
     * então setar sua afinidade e seu antigeno. Atualiza afinidade de um
     * anticorpo.
     *
     * @param anticorposs Lista de anticorpos para atualizar a afinidade.
     */
    public static void atualizaAfinidadePop(ArrayList<Anticorpo> anticorpos) {
//        ArrayList<Anticorpo> anticorpos = new ArrayList<>();
//        anticorpos.addAll(anticorposs);
//        ArrayList<Anticorpo> anticorpos = (ArrayList<Anticorpo>)anticorposs.clone();
//        Collections.shuffle(getAntigenos()); //Fisher–Yates shuffle @https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
//        for (int i = 0; i < Math.min(anticorposs.size(), antigenos.size()); i++) {// for de antigenos
        for (int i = 0; i < antigenos.size(); i++) {// for de antigenos
            Auxx aux = new Auxx(Double.MIN_VALUE); // Guarda o anticorpo de menor distância, e a menor distância.
            aux.setAnticorpo(null); // inicializa um anticorpo para o aux
            for (Anticorpo anticorpo : anticorpos) { // Encontrar o anticorpo de maior afinidade
                double dist = 1 / distanciaEuclidiana(anticorpo, antigenos.get(i));
                //quanto maior afinidade melhor
                if (dist > aux.getDist()) { // atualiza quem tem maior afinidade
                    if (dist > anticorpo.getAfinidade() && anticorpo.getAntigeno() != null) {
                        aux.setDist(dist);
                        aux.setAnticorpo(anticorpo);
                    }
                    if (anticorpo.getAntigeno() == null) {
                        aux.setDist(dist);
                        aux.setAnticorpo(anticorpo);
                    }

                }
            }

            // atualiza afinidade
            double afinidade = 0;
            if (aux.getAnticorpo() != null) {
                afinidade = aux.getDist();
                aux.getAnticorpo().setAfinidade(afinidade);
                aux.getAnticorpo().setAntigeno(antigenos.get(i));
            }

        }
        // Depois que calcula todas afinidades, normalizar todas as diferentes de -1
        //Pegando máximo e mínimo
        double somatorio = 0;
        for (Anticorpo anticorpo : anticorpos) {
            if (anticorpo.getAfinidade() != -1) {
//                System.out.println("Afinidade: "+anticorpo.getAfinidade());
                somatorio += anticorpo.getAfinidade();
            }
        }
//        System.out.println("Afinidades Antigas: ");
//        System.out.println(anticorposs.toString());
//         Setanto afinidades normalizadas
        for (Anticorpo anticorpo : anticorpos) {
            if (anticorpos.size() == 1) {
//                System.out.println("caiu: "+anticorpo.toString());
                anticorpo.setAfinidade(0.3); // único entao 0
            } else if (anticorpo.getAfinidade() == -1) {
                anticorpo.setAfinidade((anticorpo.getAfinidade())); // -1 continua -1
            } else {
                anticorpo.setAfinidade((anticorpo.getAfinidade()) / (somatorio));
            }
        }
//        System.out.println("Max: "+max+" Min: "+min);
//        System.out.println("Afinidades Normalizadas: ");
//        System.out.println(anticorposs.toString());
    }

    /**
     * Retorna um clone de um anticorpo. Clone = (randn*Ab+Ab)/2
     *
     * @param ant anticorpo que deseja clonar.
     * @return retorna um clone de um anticorpo.
     */
    public static Anticorpo clone(Anticorpo ant) {
        // Gets
        Anticorpo clone = new Anticorpo();
        double afinidade = ant.getAfinidade();
        ArrayList<Double> varsClone = (ArrayList<Double>) ant.getVars().clone();
        // Sets Vars
        for (int i = 0; i < tamanhoBase; i++) {
//            double x = Math.abs((((1 - r.nextGaussian()) * varsClone.get(i)) + varsClone.get(i)) / 2); // antigo
            double x = Math.abs((((r.nextGaussian()) * varsClone.get(i)) + varsClone.get(i))); // teste (ficou melhor)
            if (x > 1) {
                varsClone.set(i, (double) 1);
            } else if (x < 0) {
                varsClone.set(i, (double) 0);
            } else {
                varsClone.set(i, x);
            }
        }

        // Sets      
        clone.setVars(varsClone);
        clone.setAfinidade(afinidade);
        clone.setAntigeno(ant.getAntigeno());
//        clone.setAntigeno(null);
        return clone;
    }

    /**
     * Muta um anticorpo e não atualiza a afinidade.
     *
     * @param anticorpo anticorpo que deseja mutar.
     */
    public static void mutaAnticorpo(Anticorpo anticorpo) {
        // A maturação (mutação) dos clones pode ser dada deslocando o anticorpo na direção do antigeno 
        // (anticorpo = anticorpo - alfa*(anticorpo-antigeno)), ponderada por uma taxa de mutação alfa.
        // alfa = (afinidade)
        for (int i = 0; i < tamanhoBase; i++) {
//            anticorpo.getVars().set(i,r.nextDouble()); // randomico
            anticorpo.getVars().set(i, anticorpo.getVars().get(i) - ((1 - anticorpo.getAfinidade()) * (anticorpo.getVars().get(i) - anticorpo.getAntigeno().getVars().get(i))));
        }
    }

    /**
     * Muta um Array de Clones.
     *
     * @param clones Array de clones que deseja mutar.
     */
    public static void mutaClones(ArrayList<Anticorpo> clones) {
        for (int i = 0; i < clones.size(); i++) {
            mutaAnticorpo(clones.get(i));
        }
    }

    /**
     * Executa o k-médias.
     */
    public static void kMedias() {
        // k-médias
        if (getPopulacao().isEmpty()) {
            System.out.println("POPULAÇÂO VAZIA!");
        }
        Matriz matriz = new Matriz(getPopulacao(), getAntigenos());
        int m[][] = matriz.criaMatriz(false);
        // Pega uma coluna e percorre suas linhas
        for (int k = 0; k < getPopulacao().size(); k++) { //coluna (Anticorpos/Protótipos)
            int count = 0;
            ArrayList<Double> aux = new ArrayList<>();
            for (int i = 0; i < tamanhoBase; i++) {
                aux.add((double) 0);
            }
            for (int j = 0; j < getAntigenos().size(); j++) { // linha (Antigenos/Objetos)
                if (m[j][k] == 1) {
                    // Pegar todos objetos e fazer a média e setar no protótipo
                    count++;
                    for (int v = 0; v < tamanhoBase; v++) {
                        aux.set(v, aux.get(v) + getAntigenos().get(j).getVars().get(v));
                    }
//                    x = x + getAntigenos().get(j).getX();
//                    y = y + getAntigenos().get(j).getY();
                }
            }
            if (count >= 2) {
//                System.out.println("Count = " + count + " X: " + getPopulacao().get(k).getX() + " nX: " + x / count + " somaX: " + x);
                for (int v = 0; v < tamanhoBase; v++) {
                    getPopulacao().get(k).getVars().set(v, aux.get(v) / count);
                }
//                getPopulacao().get(k).setX(x / count);
//                getPopulacao().get(k).setY(y / count);
            }
        }
    }

    /**
     * @deprecated @param limite
     */
    public void mediasInt(double limite) {

        ArrayList<Anticorpo> anticorpos = new ArrayList<>();
        // Pegar um anticorpo aleatório e medir distâncias perto dele
        double min = 0;
        double max = Math.sqrt(tamanhoBase);

        for (int i = 0; i < populacao.size(); i++) {
            Anticorpo sel = populacao.get(r.nextInt(populacao.size()));
//            System.out.println("Selecionado: " + sel.toString());
            for (Anticorpo anticorpo : getPopulacao()) {
                double soma = 0;
                for (int v = 0; v < tamanhoBase; v++) {
                    // distância eucliadina
                    soma += Math.pow(sel.getVars().get(v) - anticorpo.getVars().get(v), 2);
                }
                double distEuclidiana = Math.sqrt(soma);
                distEuclidiana = (distEuclidiana - min) / (max - min); // normalizando a distância
                if (distEuclidiana <= limite && distEuclidiana != 0) {
                    anticorpos.add(anticorpo);
                }
            }
            // Faz média
//            double somaX = 0, somaY = 0;
            ArrayList<Double> somas = new ArrayList<>();
            for (Anticorpo anticorpo : anticorpos) {
                // inicializa com zeros
                for (int v = 0; v < tamanhoBase; v++) {
                    somas.add((double) 0);
                }
                // somatorios
                for (int v = 0; v < tamanhoBase; v++) {
                    somas.set(v, somas.get(v) + anticorpo.getVars().get(v));
                }
//                somaX = somaX + anticorpo.getX();
//                somaY = somaY + anticorpo.getY();
            }
//            System.out.println("SomaX: " + somaX + " SomaY: " + somaY);
//            if (somaX != 0 || somaY != 0) {
            if (verificaZeros(somas)) {
//                System.out.println("X: " + somaX / anticorpos.size() + " Y: " + somaY / anticorpos.size());
                for (int v = 0; v < tamanhoBase; v++) {
                    sel.getVars().set(v, somas.get(v) / anticorpos.size());
                }

//                sel.setX(somaX / anticorpos.size());
//                sel.setY(somaY / anticorpos.size());
                populacao.removeAll(anticorpos);
            }

            anticorpos.removeAll(anticorpos);
        }
    }

    /**
     * @deprecated @param somas
     * @return
     */
    public boolean verificaZeros(ArrayList<Double> somas) {
        boolean zero = false;
        for (Double soma : somas) {
            // se algum diferente de zero então retorna true
            if (soma != 0) {
                zero = true;
            }
        }
        return zero;
    }

    //OK
    /**
     * Calcula o PCC do algrotimo.
     *
     * @return PCC
     */
    public static double matrizGrupos() {
        // Número de rótulos
        int max = 0;
        for (Antigeno antigeno : antigenos) {
            if (antigeno.getRotulo() > max) {
                max = antigeno.getRotulo();
            }
        }

        int matriz[][] = new int[getPopulacao().size()][max];
        //inicializa matriz com zeros
        for (int i = 0; i < getPopulacao().size(); i++) {
            for (int j = 0; j < max; j++) {
                matriz[i][j] = 0;
            }
        }

        Matriz matrix = new Matriz(getPopulacao(), getAntigenos());
        int[][] m = matrix.criaMatriz(false); //antigeno/anticorpo

        // pegar uma coluna e ver todas linhas
        for (int j = 0; j < getPopulacao().size(); j++) { //linhas
            for (int i = 0; i < getAntigenos().size(); i++) { //colunas
                for (int k = 0; k < max; k++) { //rotulos
                    if (m[i][j] == 1) {
                        if (getAntigenos().get(i).getRotulo() == k + 1) {
                            matriz[j][k] += 1;
                        }
                    }
                }
            }
        }

        //imprime matriz
        // extrair max de cada linha
        int soma = 0;
        for (int i = 0; i < getPopulacao().size(); i++) {
            int maior = -1;
            for (int j = 0; j < max; j++) {
//                System.out.print("\t" + matriz[i][j]);
                if (matriz[i][j] > maior) {
                    maior = matriz[i][j];
                }
            }
            soma += maior;
//            System.out.println("");
        }
//        System.out.println("PCC: " + df.format((double) ((double) soma / (double) getAntigenos().size()) * 100));
        return (double) ((double) soma / (double) getAntigenos().size()) * 100;
    }

    /**
     * ONDE RODA O CLONALG
     *
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // Gera população
        Clonalg clo = new Clonalg();
        ArrayList<Anticorpo> iniciais = clo.geraPop(numExecu);
        ArrayList<Anticorpo> iniciaisE = clo.geraPop(numExecuEnsemble);

        clo.executa(iniciais);

        for (int i = 0; i < ensembles.size(); i++) {
            for (float j = (float) 0.02; j <= 0.10; j += 0.02) {
//                System.out.println("teste " + i);
                Ensemble aux = ensembles.get(i);
                ensembles.get(i).getRoleta().add(roletaE(aux, j, iniciaisE.get(i)));

            }
        }

        for (int i = 0; i < ensembles.size(); i++) {
            for (float j = (float) 0.02; j <= 0.10; j += 0.02) {
//                System.out.println("teste " + i);
                Ensemble aux = ensembles.get(i);
                ensembles.get(i).getElitismo().add(elitismoE(aux, j, iniciaisE.get(i)));

            }
        }

//        System.out.println("\nFinal Elitismo");
//        for (Ensemble ensemble : ensembles) {
//            System.out.println("melhor limiar " + ensemble.getMelhorlimiar());
//            for (int i = 0; i < ensemble.getElitismo().size(); i++) {
//                System.out.println("Erro " + ensemble.getElitismo().get(i).getErroQ()
//                        + " Limiar " + ensemble.getElitismo().get(i).getLimiar() + " PCC "
//                        + ensemble.getElitismo().get(i).getPcc() + " n prot "
//                        + ensemble.getElitismo().get(i).getPrototipos().size());
//
//            }
//        }
//
////        
//        System.out.println("\n\n\n\nFinal Roleta");
//        for (Ensemble ensemble : ensembles) {
//            System.out.println("melhor limiar " + ensemble.getMelhorlimiar());
//            for (int i = 0; i < ensemble.getRoleta().size(); i++) {
//                System.out.println("Erro " + ensemble.getRoleta().get(i).getErroQ()
//                        + " Limiar " + ensemble.getRoleta().get(i).getLimiar() + " PCC "
//                        + ensemble.getRoleta().get(i).getPcc() + " n prot "
//                        + ensemble.getRoleta().get(i).getPrototipos().size());
//            }
//        }
        //dados gerais
        DecimalFormat dfe = new DecimalFormat("#.##");
        DecimalFormat dferro = new DecimalFormat("#.#######");
        for (Ensemble ensemble : ensembles) {
            int menorp = 1000, maiorp = 0;
            double mediap = 0;
            double piorEQ = 0, melhorEQ = 1000, mediaEQ = 0;
            double melhorPCC = 0, piorPCC = 100, mediaPCC = 0;
            ArrayList<Double> desvioPCCE = new ArrayList();
            ArrayList<Double> desvioProE = new ArrayList();
            ArrayList<Double> desvioErroQ = new ArrayList();
            for (Particao particoe : ensemble.getParticoes()) {
                if (menorp > particoe.getPrototipos().size()) {
                    menorp = particoe.getPrototipos().size();
                }
                if (maiorp < particoe.getPrototipos().size()) {
                    maiorp = particoe.getPrototipos().size();
                }
                desvioProE.add((double) particoe.getPrototipos().size());
                mediap += particoe.getPrototipos().size();

                if (melhorPCC < particoe.getPcc()) {
                    melhorPCC = particoe.getPcc();
                }
                if (piorPCC > particoe.getPcc()) {
                    piorPCC = particoe.getPcc();
                }
                desvioPCCE.add((double) particoe.getPcc());
                mediaPCC += particoe.getPcc();

                if (piorEQ < particoe.getErroQ()) {
                    piorEQ = particoe.getErroQ();
                }
                if (melhorEQ > particoe.getErroQ()) {
                    melhorEQ = particoe.getErroQ();
                }
                desvioErroQ.add((double) particoe.getErroQ());
                mediaEQ += particoe.getErroQ();
            }

            System.out.println(menorp + " " + dfe.format(mediap / ensemble.getParticoes().size()) + "±" + dfe.format(desvioPadrao(desvioProE, mediap / ensemble.getParticoes().size())) + " " + maiorp + " "
                    + dfe.format(piorPCC) + " " + dfe.format(mediaPCC / ensemble.getParticoes().size()) + "±" + dfe.format(desvioPadrao(desvioPCCE, mediaPCC / ensemble.getParticoes().size())) + " " + dfe.format(melhorPCC) + " "
                    + dferro.format(piorEQ) + " " + dferro.format(mediaEQ / ensemble.getParticoes().size()) + "±" + dferro.format(desvioPadrao(desvioErroQ, mediaEQ / ensemble.getParticoes().size())) + " " + dferro.format(melhorEQ) + " "
                    + ensemble.getMelhorlimiar() + " " + ensemble.getElitismo().get(0).getPrototipos().size() + " " + dfe.format(ensemble.getElitismo().get(0).getPcc()) + " " + dferro.format(ensemble.getElitismo().get(0).getErroQ()) + " "
                    + ensemble.getElitismo().get(1).getPrototipos().size() + " " + dfe.format(ensemble.getElitismo().get(1).getPcc()) + " " + dferro.format(ensemble.getElitismo().get(1).getErroQ()) + " "
                    + ensemble.getElitismo().get(2).getPrototipos().size() + " " + dfe.format(ensemble.getElitismo().get(2).getPcc()) + " " + dferro.format(ensemble.getElitismo().get(2).getErroQ()) + " "
                    + ensemble.getElitismo().get(3).getPrototipos().size() + " " + dfe.format(ensemble.getElitismo().get(3).getPcc()) + " " + dferro.format(ensemble.getElitismo().get(3).getErroQ()) + " "
                    + ensemble.getRoleta().get(0).getPrototipos().size() + " " + dfe.format(ensemble.getRoleta().get(0).getPcc()) + " " + dferro.format(ensemble.getRoleta().get(0).getErroQ()) + " "
                    + ensemble.getRoleta().get(1).getPrototipos().size() + " " + dfe.format(ensemble.getRoleta().get(1).getPcc()) + " " + dferro.format(ensemble.getRoleta().get(1).getErroQ()) + " "
                    + ensemble.getRoleta().get(2).getPrototipos().size() + " " + dfe.format(ensemble.getRoleta().get(2).getPcc()) + " " + dferro.format(ensemble.getRoleta().get(2).getErroQ()) + " "
                    + ensemble.getRoleta().get(3).getPrototipos().size() + " " + dfe.format(ensemble.getRoleta().get(3).getPcc()) + " " + dferro.format(ensemble.getRoleta().get(3).getErroQ())
            );
        }
        System.out.println("\n\n\nelitismo");
        //dados consenso
        int menorp = 1000, maiorp = 0;
        double mediap = 0;
        double piorEQ = 0, melhorEQ = 1000, mediaEQ = 0;
        double melhorPCC = 0, piorPCC = 100, mediaPCC = 0;

        ArrayList<Double> desvioPCCE = new ArrayList();
        ArrayList<Double> desvioProE = new ArrayList();
        ArrayList<Double> desvioErroQ = new ArrayList();

        //elitismo
        for (int i = 0; i < 4; i++) {

            for (Ensemble ensemble : ensembles) {

                if (menorp > ensemble.getElitismo().get(i).getPrototipos().size()) {
                    menorp = ensemble.getElitismo().get(i).getPrototipos().size();
                }
                if (maiorp < ensemble.getElitismo().get(i).getPrototipos().size()) {
                    maiorp = ensemble.getElitismo().get(i).getPrototipos().size();
                }
                desvioProE.add((double) ensemble.getElitismo().get(i).getPrototipos().size());
                mediap += ensemble.getElitismo().get(i).getPrototipos().size();

                if (melhorPCC < ensemble.getElitismo().get(i).getPcc()) {
                    melhorPCC = ensemble.getElitismo().get(i).getPcc();
                }
                if (piorPCC > ensemble.getElitismo().get(i).getPcc()) {
                    piorPCC = ensemble.getElitismo().get(i).getPcc();
                }
                desvioPCCE.add((double) ensemble.getElitismo().get(i).getPcc());
                mediaPCC += ensemble.getElitismo().get(i).getPcc();

                if (piorEQ < ensemble.getElitismo().get(i).getErroQ()) {
                    piorEQ = ensemble.getElitismo().get(i).getErroQ();
                }
                if (melhorEQ > ensemble.getElitismo().get(i).getErroQ()) {
                    melhorEQ = ensemble.getElitismo().get(i).getErroQ();
                }
                desvioErroQ.add((double) ensemble.getElitismo().get(i).getErroQ());
                mediaEQ += ensemble.getElitismo().get(i).getErroQ();
            }
            System.out.println(menorp + " " + dfe.format(mediap / ensembles.size()) + "±" + dfe.format(desvioPadrao(desvioProE, mediap / ensembles.size())) + " " + maiorp + " "
                    + dfe.format(piorPCC) + " " + dfe.format(mediaPCC / ensembles.size()) + "±" + dfe.format(desvioPadrao(desvioPCCE, mediaPCC / ensembles.size())) + " " + dfe.format(melhorPCC) + " "
                    + dferro.format(piorEQ) + " " + dferro.format(mediaEQ / ensembles.size()) + "±" + dferro.format(desvioPadrao(desvioErroQ, mediaEQ / ensembles.size())) + " " + dferro.format(melhorEQ) + " ");

            menorp = 1000;
            maiorp = 0;
            mediap = 0;
            piorEQ = 0;
            melhorEQ = 1000;
            mediaEQ = 0;
            melhorPCC = 0;
            piorPCC = 100;
            mediaPCC = 0;

            desvioPCCE.clear();
            desvioProE.clear();
            desvioErroQ.clear();
        }

        System.out.println("\n\n\nroleta");
        //dados consenso

        //roleta
        for (int i = 0; i < 4; i++) {

            for (Ensemble ensemble : ensembles) {

                if (menorp > ensemble.getRoleta().get(i).getPrototipos().size()) {
                    menorp = ensemble.getRoleta().get(i).getPrototipos().size();
                }
                if (maiorp < ensemble.getRoleta().get(i).getPrototipos().size()) {
                    maiorp = ensemble.getRoleta().get(i).getPrototipos().size();
                }
                desvioProE.add((double) ensemble.getRoleta().get(i).getPrototipos().size());
                mediap += ensemble.getRoleta().get(i).getPrototipos().size();

                if (melhorPCC < ensemble.getRoleta().get(i).getPcc()) {
                    melhorPCC = ensemble.getRoleta().get(i).getPcc();
                }
                if (piorPCC > ensemble.getRoleta().get(i).getPcc()) {
                    piorPCC = ensemble.getRoleta().get(i).getPcc();
                }
                desvioPCCE.add((double) ensemble.getRoleta().get(i).getPcc());
                mediaPCC += ensemble.getRoleta().get(i).getPcc();

                if (piorEQ < ensemble.getRoleta().get(i).getErroQ()) {
                    piorEQ = ensemble.getRoleta().get(i).getErroQ();
                }
                if (melhorEQ > ensemble.getRoleta().get(i).getErroQ()) {
                    melhorEQ = ensemble.getRoleta().get(i).getErroQ();
                }
                desvioErroQ.add((double) ensemble.getRoleta().get(i).getErroQ());
                mediaEQ += ensemble.getRoleta().get(i).getErroQ();
            }
            System.out.println(menorp + " " + dfe.format(mediap / ensembles.size()) + "±" + dfe.format(desvioPadrao(desvioProE, mediap / ensembles.size())) + " " + maiorp + " "
                    + dfe.format(piorPCC) + " " + dfe.format(mediaPCC / ensembles.size()) + "±" + dfe.format(desvioPadrao(desvioPCCE, mediaPCC / ensembles.size())) + " " + dfe.format(melhorPCC) + " "
                    + dferro.format(piorEQ) + " " + dferro.format(mediaEQ / ensembles.size()) + "±" + dferro.format(desvioPadrao(desvioErroQ, mediaEQ / ensembles.size())) + " " + dferro.format(melhorEQ) + " ");

            menorp = 1000;
            maiorp = 0;
            mediap = 0;
            piorEQ = 0;
            melhorEQ = 1000;
            mediaEQ = 0;
            melhorPCC = 0;
            piorPCC = 100;
            mediaPCC = 0;

            desvioPCCE.clear();
            desvioProE.clear();
            desvioErroQ.clear();
        }

    }

    public static Particao roletaE(Ensemble aux, float percentagem, Anticorpo inicial) {
        double valormaximo = 0, valoraux = 0, valoraleatorio = 0;
        double swap = 0;
        int cont = 0, x = 0;
        boolean verifica = true;
        Random r = new Random();
        ArrayList<Particao> aux1 = new ArrayList();
        aux1.clear();
        ArrayList<Anticorpo> testes = null;
        ArrayList<Particao> escolhidos = new ArrayList();
        //limpa os antigenos
        antigenos.clear();

        for (Particao particoe : aux.getParticoes()) {
            Particao teste = new Particao(particoe.getPrototipos(), particoe.getErroQ(), particoe.getLimiar(), particoe.getPcc());
            aux1.add(teste);
        }

        //inverte valores ERROQ //ok
        for (Particao particoe : aux1) {
            particoe.setErroQ(1 / (particoe.getErroQ()));
        }

        while (escolhidos.size() < (int) aux.getParticoes().size() * percentagem) {
            //soma todos os valores
            valormaximo = 0;
            for (Particao particao : aux1) {
                valormaximo += particao.getErroQ();

            }
            cont = 0;
            valoraux = 0;
            valoraleatorio = r.nextDouble() * valormaximo;
            while (valoraux < valoraleatorio) {
                valoraux += aux1.get(cont).getErroQ();
                cont++;
            }
            Particao auxP = aux.getParticoes().get(cont - 1);

            if (escolhidos.contains(auxP)) {
            } else {
                escolhidos.add(auxP);
                aux1.remove(auxP);
            }
        }
        int contador = 0;
        int contadoriguais = 0;
        //transforma escolhidos em antigenos
        for (Particao escolhido : escolhidos) {
            for (Anticorpo prototipo : escolhido.getPrototipos()) {
                contador = 0;
                Antigeno ant = new Antigeno(prototipo.getVars(), prototipo.getAntigeno().getRotulo());
                for (Antigeno antigeno : antigenos) {
                    if (antigeno.getVars() == ant.getVars() && antigeno.getRotulo() == ant.getRotulo()) {
                        contador++;
                    } else {

                    }
                }
                if (contador == 0) {
                    antigenos.add(ant);
                } else {
                    contadoriguais++;
                }
            }
        }
        //System.out.println("tamanho base: " + antigenos.size() + " excluidos " + excluidos.size());
        //System.out.println("qtd antigenos roleta " + antigenos.size() + " qtd selecionados" + escolhidos.size());
        //adiciona anticorpo inicial na populacao e guarda ele separado
        int gers = 0;
        getPopulacao().removeAll(getPopulacao());
        getPopulacao().add(inicial);
        ArrayList<Anticorpo> popInicialE = new ArrayList<>();
        Anticorpo inicialE = new Anticorpo((ArrayList<Double>) getPopulacao().get(0).getVars().clone());
        popInicialE.add(inicialE);

        double objetivoAtual = 0;
        double objetivoDiferenca = 0;
        double objetivoAnterior = 0;

        atualizaAfinidadePop(getPopulacao());
        for (int i = 0; i < numGeracoes; i++) {
            objetivoAnterior = objetivoAtual;
            if (objetivoDiferenca > erroQuadratico || i == 0) {
                objetivoDiferenca = objetivoAtual; // a objetivo é atualizada
                ArrayList<Anticorpo> selecionados = new ArrayList<>();

                for (Anticorpo anticorpo : getPopulacao()) {
                    if ((anticorpo.getAfinidade() > aux.getMelhorlimiar() || i == 0) && anticorpo.getAfinidade() != -1) { // limiar
                        int nClones = (int) (numClo * (anticorpo.getAfinidade())); // modificado 1- anti
                        if (nClones < 1) {
                            nClones = 1;
                        }

                        for (int k = 0; k < nClones; k++) { // insere k vezes o mesmo elemento na lista de clones
                            selecionados.add(clone(anticorpo));
                        }
                    }
                }
                mutaClones(selecionados);
                selecionaMelhoresClones(selecionados); //6,7
                getPopulacao().addAll(selecionados); //8
                kMedias();
                for (Anticorpo anticorpo : getPopulacao()) {
                    anticorpo.setAntigeno(null);
                    anticorpo.setAfinidade((double) -1);
                }
                atualizaAfinidadePop(getPopulacao());
                ArrayList<Anticorpo> deletss = new ArrayList<>();
                for (Anticorpo anticorpo : getPopulacao()) {
                    if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
                        deletss.add(anticorpo);
                    }
                }
                getPopulacao().removeAll(deletss);

                // 9) Calcular o erro quadrático.
                // Calcula função objetivo (Erro Quadrático)
                objetivoAtual = 0;
                for (Antigeno antigeno : getAntigenos()) {
                    for (Anticorpo anticorpo : getPopulacao()) {
                        if (anticorpo.getAntigeno() == antigeno) {
                            objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                        } else {
                            objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                        }
                    }
                }
                objetivoDiferenca = Math.abs(objetivoAnterior - objetivoAtual);

                gers = i + 1;

            } else {
                gers = i + 1;
                i = numGeracoes; // finaliza o alg.
            }

        }//fim clonalg

        //imprime particao consenso na base de selecao
        PlotTest plot = new PlotTest(popInicialE, getAntigenos(), getPopulacao(), "Roleta");
        //atualiza dados para gerar pcc e erroq em relação a base original
        antigenos.clear();
        antigenos = leitor.leAntigenos();
        atualizaAfinidadePop(getPopulacao());

        objetivoAtual = 0;
        for (Antigeno antigeno : getAntigenos()) {
            for (Anticorpo anticorpo : getPopulacao()) {
                if (anticorpo.getAntigeno() == antigeno) {
                    objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                } else {
                    objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                }
            }
        }

        double PCC = matrizGrupos();
        ArrayList<Anticorpo> auxE = new ArrayList<>();
        auxE.addAll(getPopulacao());

        Particao consensoRoleta = new Particao(auxE, objetivoAtual, aux.getMelhorlimiar(), PCC);

        return consensoRoleta;
    }

    //elitismo OK
    public static Particao elitismoE(Ensemble aux, float percentagem, Anticorpo inicial) {
        QuickSort.quickSortErro(aux.getParticoes());
        ArrayList<Anticorpo> testes = null;
        ArrayList<Particao> escolhidos = new ArrayList();
        //limpa os antigenos
        antigenos.clear();

        for (int i = 0; i < aux.getParticoes().size() * percentagem; i++) {
//            System.out.println("escolheu " + aux.getParticoes().get(i).getErroQ()+" qtd "+ aux.getParticoes().get(i).getPrototipos().size());
            Particao auxP = aux.getParticoes().get(i);
            //escolho as particoes para segunda etapa
            escolhidos.add(auxP);
        }
        int contador = 0;
        int contadoriguais = 0;
        //transformo escolhidos em antigenos
        for (Particao escolhido : escolhidos) {
            for (Anticorpo prototipo : escolhido.getPrototipos()) {
                contador = 0;
                Antigeno ant = new Antigeno(prototipo.getVars(), prototipo.getAntigeno().getRotulo());
                for (Antigeno antigeno : antigenos) {
                    if (antigeno.getVars() == ant.getVars() && antigeno.getRotulo() == ant.getRotulo()) {
                        contador++;

                    } else {

                    }
                }
                if (contador == 0) {
                    antigenos.add(ant);
                } else {
                    contadoriguais++;
                }

            }
        }
//        if(antigenos.size()!=0){
//        PlotTest plot = new PlotTest(antigenos);}
        //rodo clonalg gerando uma particao

        //adiciona anticorpo inicial na populacao e guarda ele separado
        int gers = 0;
        getPopulacao().removeAll(getPopulacao());
        getPopulacao().add(inicial);
        ArrayList<Anticorpo> popInicialE = new ArrayList<>();
        Anticorpo inicialE = new Anticorpo((ArrayList<Double>) getPopulacao().get(0).getVars().clone());
        popInicialE.add(inicialE);

        double objetivoAtual = 0;
        double objetivoDiferenca = 0;
        double objetivoAnterior = 0;

        atualizaAfinidadePop(getPopulacao());
        for (int i = 0; i < numGeracoes; i++) {
            objetivoAnterior = objetivoAtual;
            if (objetivoDiferenca > erroQuadratico || i == 0) {
                objetivoDiferenca = objetivoAtual; // a objetivo é atualizada
                ArrayList<Anticorpo> selecionados = new ArrayList<>();

                for (Anticorpo anticorpo : getPopulacao()) {
                    if ((anticorpo.getAfinidade() > aux.getMelhorlimiar() || i == 0) && anticorpo.getAfinidade() != -1) { // limiar
                        int nClones = (int) (numClo * (anticorpo.getAfinidade())); // modificado 1- anti
                        if (nClones < 1) {
                            nClones = 1;
                        }

                        for (int k = 0; k < nClones; k++) { // insere k vezes o mesmo elemento na lista de clones
                            selecionados.add(clone(anticorpo));
                        }
                    }
                }
                mutaClones(selecionados);
                selecionaMelhoresClones(selecionados); //6,7
                getPopulacao().addAll(selecionados); //8
                kMedias();
                for (Anticorpo anticorpo : getPopulacao()) {
                    anticorpo.setAntigeno(null);
                    anticorpo.setAfinidade((double) -1);
                }
                atualizaAfinidadePop(getPopulacao());
                ArrayList<Anticorpo> deletss = new ArrayList<>();
                for (Anticorpo anticorpo : getPopulacao()) {
                    if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
                        deletss.add(anticorpo);
                    }
                }
                getPopulacao().removeAll(deletss);

                // 9) Calcular o erro quadrático.
                // Calcula função objetivo (Erro Quadrático)
                objetivoAtual = 0;
                for (Antigeno antigeno : getAntigenos()) {
                    for (Anticorpo anticorpo : getPopulacao()) {
                        if (anticorpo.getAntigeno() == antigeno) {
                            objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                        } else {
                            objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                        }
                    }
                }
                objetivoDiferenca = Math.abs(objetivoAnterior - objetivoAtual);

                gers = i + 1;

            } else {

                gers = i + 1;
                i = numGeracoes; // finaliza o alg.
            }

        }//fim clonalg

        //nova etapa nivelamento------------------------------------
        ArrayList<Integer> somas = new ArrayList<>();
        float nivel = 0;
        ArrayList<Anticorpo> quemsai = new ArrayList<>();
        ArrayList<Anticorpo> selecionadosN = new ArrayList<>();

        nivel = antigenos.size() / getPopulacao().size();
        Matriz matriz = new Matriz(getPopulacao(), getAntigenos());
        somas = matriz.pegasoma();
        for (int i = 0; i < somas.size(); i++) {
            if ((somas.get(i)) > nivel * 1.5) {
                quemsai.add(getPopulacao().get(i));
                for (int j = 0; j < 2; j++) {
                    selecionadosN.add(clone(getPopulacao().get(i)));
                }
            }
        }

        getPopulacao().removeAll(quemsai);
        mutaClones(selecionadosN);
        selecionaMelhoresClones(selecionadosN); //6,7
        getPopulacao().addAll(selecionadosN); //8
        kMedias();
        for (Anticorpo anticorpo : getPopulacao()) {
            anticorpo.setAntigeno(null);
            anticorpo.setAfinidade((double) -1);
        }
        atualizaAfinidadePop(getPopulacao());
        ArrayList<Anticorpo> deletss = new ArrayList<>();
        for (Anticorpo anticorpo : getPopulacao()) {
            if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
                deletss.add(anticorpo);
            }
        }
        getPopulacao().removeAll(deletss);
        objetivoAtual = 0;
        for (Antigeno antigeno : getAntigenos()) {
            for (Anticorpo anticorpo : getPopulacao()) {
                if (anticorpo.getAntigeno() == antigeno) {
                    objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                } else {
                    objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                }
            }
        }
        objetivoDiferenca = Math.abs(objetivoAnterior - objetivoAtual);

        somas.clear();
        Matriz matrizA = new Matriz(getPopulacao(), getAntigenos());
        ArrayList<Anticorpo> sai = new ArrayList<>();
        somas = matrizA.pegasoma();
        for (int i = 0; i < somas.size(); i++) {
            if (somas.get(i) == 0) {
                sai.add(getPopulacao().get(i));
            }
        }
        getPopulacao().removeAll(sai);
        //imprime particao consenso na base de selecao
        PlotTest plot = new PlotTest(popInicialE, getAntigenos(), getPopulacao(), "Elitismo");
        //atualiza dados para gerar pcc e erroq em relação a base original
        antigenos.clear();
        antigenos = leitor.leAntigenos();
        atualizaAfinidadePop(getPopulacao());

        objetivoAtual = 0;
        for (Antigeno antigeno : getAntigenos()) {
            for (Anticorpo anticorpo : getPopulacao()) {
                if (anticorpo.getAntigeno() == antigeno) {
                    objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                } else {
                    objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                }
            }
        }

        double PCC = matrizGrupos();
        ArrayList<Anticorpo> auxE = new ArrayList<>();
        auxE.addAll(getPopulacao());
        Particao consensoElitismo = new Particao(auxE, objetivoAtual, aux.getMelhorlimiar(), PCC);

        //imprime particao consenso na base original
        //plot = new PlotTest(popInicialE, getAntigenos(), consensoElitismo.getPrototipos(), "Elitismo");
        escolhidos.clear();
        return consensoElitismo;
    }

    /**
     * Atualiza um ArrayList deixando somente os clones mais adaptados.
     *
     * @param clones
     */
    public static void selecionaMelhoresClones(ArrayList<Anticorpo> clones) {
        ArrayList<Anticorpo> rejeitados = new ArrayList<>();
        for (Anticorpo clone : clones) {
            double dist = distanciaEuclidiana(clone, clone.getAntigeno());
            clone.setAfinidade(dist);
        }
        quick.quickSort(clones); // Ordem crescete, melhores por primeiro

        int reSel = (int) (clones.size() * numSel); // selecionando x% melhores
        if (reSel < 1) {
            reSel = 1;
        }

        for (int j = reSel; j < clones.size(); j++) {
            rejeitados.add(clones.get(j));
        }
        clones.removeAll(rejeitados);
    }

    /**
     * @deprecated Pega os anticorpos que classificam o mesmo objeto e fazem
     * média entre si.
     */
    public void mediaMesmosGrupos() {
        ArrayList<Anticorpo> novos = new ArrayList<>();
        ArrayList<Anticorpo> remover = new ArrayList<>();
        for (Antigeno antigeno : getAntigenos()) {
            ArrayList<Anticorpo> mesmos = new ArrayList<>();

            for (Anticorpo pop : populacao) {
                if (pop.getAntigeno() == antigeno) {
                    mesmos.add(pop);
                }
            }
            if (mesmos.size() >= 2) {
                ArrayList<Double> aux = new ArrayList<>();
                for (int i = 0; i < tamanhoBase; i++) {
                    aux.add(0.0);
                }
                for (Anticorpo mesmo : mesmos) {
                    for (int i = 0; i < tamanhoBase; i++) {
                        aux.set(i, aux.get(i) + mesmo.getVars().get(i));
                    }
                }
                for (int i = 0; i < aux.size(); i++) {
                    aux.set(i, aux.get(i) / mesmos.size());
                }
                Anticorpo a = new Anticorpo(aux, antigeno, 1); // PODE SER QUE TENHA QUE MUDAR O 1
                novos.add(a);
                remover.addAll(mesmos);
            }
        }
        populacao.removeAll(remover);
        populacao.addAll(novos);
    }

    // VAI EXECUTAR O NUMERO DE VEZES DO TAMANHO DO ARRAY EXECUCOES
    public void executa(ArrayList<Anticorpo> execucoes) throws InterruptedException {
        // COMEÇA ITERAÇÕES

        for (int e = 0; e < numExecuEnsemble / 9; e++) {
            // atributos de cada ensemble
            antigenos.clear();
            antigenos = leitor.leAntigenos();
            ArrayList<String> resultados = new ArrayList<>();
            float melhorlimiarE = 0;
            int cont = 0;
            double limi = 0.0;
            particoes.clear();
            for (int z = 0; z < 9; z++) {
                ArrayList<Double> desvioPCC = new ArrayList();
                ArrayList<Double> desvioPro = new ArrayList();
                ArrayList<Double> desvioGer = new ArrayList();
                double mediaPCC = 0, melhorPCC = 0, piorPCC = 1000.0;
                double errolimiar = 1;
                double mediaGeracoes = 0;
                double mediaPrototipos = 0;
                int maxNumProt = 0, minNumProt = Integer.MAX_VALUE, maxGer = 0, minGer = Integer.MAX_VALUE;
                int it = 1;
                limi += 0.1;
                limiar = limi;
                long start = System.currentTimeMillis();

                for (Anticorpo exec : execucoes) {
                    int gers = 0;
                    it++;
                    getPopulacao().removeAll(getPopulacao());
                    getPopulacao().add(exec);
                    ArrayList<Anticorpo> popInicial = new ArrayList<>();
                    Anticorpo inicial = new Anticorpo((ArrayList<Double>) getPopulacao().get(0).getVars().clone());
                    popInicial.add(inicial);

                    double objetivoAtual = 0;
                    double objetivoDiferenca = 0;
                    double objetivoAnterior = 0;

                    atualizaAfinidadePop(getPopulacao());
                    // Laço do algoritmo, inicia as gerações
                    for (int i = 0; i < numGeracoes; i++) {
                        objetivoAnterior = objetivoAtual;
                        if (objetivoDiferenca > erroQuadratico || i == 0) {
                            objetivoDiferenca = objetivoAtual; // a objetivo é atualizada
                            // 4) Seleciona candidatos a clones, conforme menor que o limiar. Clona de acordo com a afinidade.
                            ArrayList<Anticorpo> selecionados = new ArrayList<>();
                            // é selecionado os anticorpos que possuem afinidade > limiar
                            for (Anticorpo anticorpo : getPopulacao()) {
                                if ((anticorpo.getAfinidade() > limiar || i == 0) && anticorpo.getAfinidade() != -1) { // limiar
                                    int nClones = (int) (numClo * (anticorpo.getAfinidade())); // modificado 1- anti
                                    if (nClones < 1) {
                                        nClones = 1;
                                    }
                                    for (int k = 0; k < nClones; k++) { // insere k vezes o mesmo elemento na lista de clones
                                        selecionados.add(clone(anticorpo));
                                    }
                                }
                            }
                            // 5) Muta clones. (afinidades ficam desatualizadas). 
                            mutaClones(selecionados);
                            // 6) Comparar a distância euclidiana de cada clone com seu antígeno e ordenar os melhores.
                            // 7) Re-selecionar somente os clones mais adaptados.
                            // 8) Inserir os mais adaptados na população geral.
                            selecionaMelhoresClones(selecionados); //6,7
                            getPopulacao().addAll(selecionados); //8

                            kMedias();
                            // Atualiza a afinidade da população (Reseta afinidades)
                            for (Anticorpo anticorpo : getPopulacao()) {
                                anticorpo.setAntigeno(null);
                                anticorpo.setAfinidade((double) -1);
                            }
                            // 2) Atualiza afinidade da população, e normaliza a afinidade. (Competem entre Si).
                            atualizaAfinidadePop(getPopulacao());
                            // 3) Remove anticorpos da população que não identificam qualquer antígeno.
                            ArrayList<Anticorpo> deletss = new ArrayList<>();
                            for (Anticorpo anticorpo : getPopulacao()) {
                                if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
                                    deletss.add(anticorpo);
                                }
                            }
                            getPopulacao().removeAll(deletss);

                            // 9) Calcular o erro quadrático.
                            // Calcula função objetivo (Erro Quadrático)
                            objetivoAtual = 0;
                            for (Antigeno antigeno : getAntigenos()) {
                                for (Anticorpo anticorpo : getPopulacao()) {
                                    if (anticorpo.getAntigeno() == antigeno) {
                                        objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                                    } else {
                                        objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                                    }
                                }
                            }
                            objetivoDiferenca = Math.abs(objetivoAnterior - objetivoAtual);

                            gers = i + 1;

                        } else {
                            gers = i + 1;
                            i = numGeracoes; // finaliza o algortimo.
                        }

                    }

                    //nova etapa nivelamento------------------------------------
                    ArrayList<Integer> somas = new ArrayList<>();
                    float nivel = 0;
                    ArrayList<Anticorpo> quemsai = new ArrayList<>();
                    ArrayList<Anticorpo> selecionadosN = new ArrayList<>();

                    nivel = antigenos.size() / getPopulacao().size();
                    Matriz matriz = new Matriz(getPopulacao(), getAntigenos());
                    somas = matriz.pegasoma();
                    for (int i = 0; i < somas.size(); i++) {
                        if ((somas.get(i)) > nivel * 1.5) {
                            quemsai.add(getPopulacao().get(i));
                            for (int j = 0; j < 5; j++) {
                                selecionadosN.add(clone(getPopulacao().get(i)));
                            }
                        }
                    }

                    getPopulacao().removeAll(quemsai);
                    mutaClones(selecionadosN);
                    selecionaMelhoresClones(selecionadosN); //6,7
                    getPopulacao().addAll(selecionadosN); //8
                    kMedias();
                    for (Anticorpo anticorpo : getPopulacao()) {
                        anticorpo.setAntigeno(null);
                        anticorpo.setAfinidade((double) -1);
                    }
                    atualizaAfinidadePop(getPopulacao());
                    ArrayList<Anticorpo> deletss = new ArrayList<>();
                    for (Anticorpo anticorpo : getPopulacao()) {
                        if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
                            deletss.add(anticorpo);
                        }
                    }
                    getPopulacao().removeAll(deletss);
                    objetivoAtual = 0;
                    for (Antigeno antigeno : getAntigenos()) {
                        for (Anticorpo anticorpo : getPopulacao()) {
                            if (anticorpo.getAntigeno() == antigeno) {
                                objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                            } else {
                                objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                            }
                        }
                    }
                    objetivoDiferenca = Math.abs(objetivoAnterior - objetivoAtual);

                    // Gerar gráfico com objetos e protótipos
                    if (getAntigenos().get(0).getVars().size() == 2 && graficosF) {
                        PlotTest plot = new PlotTest(popInicial, getAntigenos(), getPopulacao(), String.valueOf(limiar));
                    }

                    if (objetivoAtual < errolimiar) {
                        errolimiar = objetivoAtual;
                    }
                    //Calculos PCC
                    double PCC = matrizGrupos();
                    desvioPCC.add(PCC);
                    mediaPCC += PCC;
                    //Calculos Gerações
                    desvioGer.add((double) gers);
                    mediaGeracoes += gers;
                    //Calculos de protótipos
                    desvioPro.add((double) getPopulacao().size());
                    mediaPrototipos += getPopulacao().size();

                    // Maxs e Mins
                    if (melhorPCC < (PCC)) {
                        melhorPCC = PCC;
                    }
                    if (piorPCC > PCC) {
                        piorPCC = PCC;
                    }
                    if (maxNumProt < getPopulacao().size()) {
                        maxNumProt = getPopulacao().size();
                    }
                    if (minNumProt > getPopulacao().size()) {
                        minNumProt = getPopulacao().size();
                    }
                    if (maxGer < gers) {
                        maxGer = gers;
                    }
                    if (minGer > gers) {
                        minGer = gers;
                    }

                    somas.clear();
                    nivel = 0;
                    nivel = antigenos.size() / getPopulacao().size();
                    Matriz matrizA = new Matriz(getPopulacao(), getAntigenos());
                    ArrayList<Anticorpo> sai = new ArrayList<>();
                    somas = matrizA.pegasoma();
                    for (int i = 0; i < somas.size(); i++) {
                        if (somas.get(i) == 0) {
                            sai.add(getPopulacao().get(i));
                        }
                    }
                    getPopulacao().removeAll(sai);

                    gers = 0;
                    int testes = (int) limiar;
                    //ok
                    ArrayList<Anticorpo> aux = new ArrayList<>();
                    aux.addAll(getPopulacao());
                    Particao particao = new Particao(aux, objetivoAtual, limiar, PCC);
                    particoes.add(particao);//90 particoes ok
                    //PlotTest plot = new PlotTest(popInicial, getAntigenos(), getPopulacao(), String.valueOf(limiar));
                    cont++;

                }//fim execucoes

                System.out.println("Limiar: " + limi);
                System.out.println("Número de Antígenos: " + getAntigenos().size());
                System.out.println("Média PCC: " + df.format(mediaPCC / execucoes.size()) + "±" + df.format(desvioPadrao(desvioPCC, mediaPCC / execucoes.size())) + "\t Min: " + df.format(piorPCC) + "\t Máx: " + df.format(melhorPCC));
                System.out.println("M. Protótipos: " + mediaPrototipos / execucoes.size() + "±" + df.format(desvioPadrao(desvioPro, mediaPrototipos / execucoes.size())) + "\t Min: " + minNumProt + "\t\t Máx: " + maxNumProt);
                System.out.println("M. Gerações: " + mediaGeracoes / execucoes.size() + "±" + df.format(desvioPadrao(desvioGer, mediaGeracoes / execucoes.size())) + "\t Min: " + minGer + "\t\t Máx: " + maxGer);
                System.out.println("Melhor Erro: " + errolimiar);

// Pra excel
                System.out.println("");
                long finish = System.currentTimeMillis();
                double time = (finish - start);
                String sb = df.format(piorPCC) + "\t" + df.format(mediaPCC / execucoes.size()) + "±" + df.format(desvioPadrao(desvioPCC, mediaPCC / execucoes.size())) + "\t" + df.format(melhorPCC) + "\t"
                        + minNumProt + "\t" + df.format(mediaPrototipos / execucoes.size()) + "±" + df.format(desvioPadrao(desvioPro, mediaPrototipos / execucoes.size())) + "\t" + maxNumProt + "\t"
                        + minGer + "\t" + df.format(mediaGeracoes / execucoes.size()) + "±" + df.format(desvioPadrao(desvioGer, mediaGeracoes / execucoes.size())) + "\t" + maxGer + "\n";
                resultados.add(sb);

                ArrayList<Particao> auxP = new ArrayList<>();
                auxP.addAll(particoes);
                System.out.println("particoes " + particoes.size());
                ArrayList<Particao> auxOrdena = new ArrayList<>();
                auxOrdena = particoes;
                melhorlimiarE = melhorLimiarE(auxOrdena);
                Ensemble ensemble = new Ensemble(auxP, melhorlimiarE);
                ensembles.add(ensemble);
                particoes.clear();

            }//fim limiares

//            System.out.println("Resutados Limiares: ");
//            for (String resultado : resultados) {
//                System.out.print(resultado);
//            }
        }//fim ensembles 

    }//fim executa

    public float melhorLimiarE(ArrayList<Particao> teste) {
        float melhorLimiar = 0, aux = 0;
        double melhorErro = 0;
        int cont = 0, aux2 = 0, aux3 = 0;
        int[] vetor = new int[9];
        for (int i = 0; i < vetor.length; i++) {
            vetor[i] = 0;
        }

        quick.quickSortErro(teste);
//        for (Particao particoe : particoes) {
//            System.out.println(particoe.getErroQ()+" "+particoe.getLimiar());
//        }
        melhorLimiar = (float) teste.get(0).getLimiar();
        melhorErro = teste.get(0).getErroQ();
        while (teste.get(cont).getErroQ() == melhorErro) {
            aux = (float) teste.get(cont).getLimiar();
            aux2 = (int) ((aux * 10) - 1);
            vetor[aux2] = vetor[aux2] + 1;
            cont++;
        }
        for (int i = 0; i < vetor.length; i++) {
            if (aux3 < vetor[i]) {
                aux3 = vetor[i];
                melhorLimiar = (float) (i + 1) / 10;
            }
        }

        return melhorLimiar;
    }

    //@link{http://www.guj.com.br/t/desvio-padrao/38312/2}
    static public double desvioPadrao(ArrayList<Double> desvio, double media) {
        double somatorio = 0;
        for (Double desv : desvio) {
            somatorio += Math.pow((desv - media), 2);
        }
        double desvioPadrao = Math.sqrt((double) ((double) 1 / ((double) desvio.size() - 1)) * somatorio);
        return desvioPadrao;
    }

    /**
     * @return the antigenos
     */
    public static ArrayList<Antigeno> getAntigenos() {
        return antigenos;
    }

    /**
     * @return the populacao
     */
    public static ArrayList<Anticorpo> getPopulacao() {
        return populacao;
    }

    /**
     * @param populacao the populacao to set
     */
    public void setPopulacao(ArrayList<Anticorpo> populacao) {
        this.populacao = populacao;
    }

    /**
     * @return the numGeracoes
     */
    public int getNumGeracoes() {
        return numGeracoes;
    }

    /**
     * @param numGeracoes the numGeracoes to set
     */
    public void setNumGeracoes(int numGeracoes) {
        this.numGeracoes = numGeracoes;
    }

}
