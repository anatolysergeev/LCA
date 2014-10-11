import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
/*
<applet code="lca" width=1000 height=400>
</applet>
*/

public class lca extends Applet implements Runnable, ActionListener {
    //main
    String standardGraph = "17\n" +
            "0 1\n" +
            "0 2\n" +
            "0 3\n" +
            "1 4\n" +
            "1 5\n" +
            "5 10\n" +
            "2 6\n" +
            "2 7\n" +
            "2 8\n" +
            "2 9\n" +
            "3 11\n" +
            "3 12\n" +
            "3 13\n" +
            "3 14\n" +
            "12 15\n" +
            "12 16";
    int appletWight = 1000;
    int appletHigh = 500;
    int vertexCount = 1;
    int vertexOne = -1;
    int vertexTwo = -1;
    int sizeSegmentTree;
    Pair[] segmentTree;
    ArrayList<Integer>[] Tree;

    //coordinates
    int[] vx;
    int[] vy;
    int[] leftBorder;
    int[] rightBorder;
    int[] parents;
    int[] sqVertex;
    int[] sqDepth;
    int[] firstVisit;

    //Color
    Color[] colorVertex;
    Color[] colorSqDepth;
    Color[] colorSqVertex;
    Color[] colorFirstVisit;
    Color line = new Color(0, 0, 0);
    Color ovalBlack = new Color(0, 0, 0);
    Color ovalGrey = new Color(145, 145, 145);
    Color digit = new Color(255, 255, 255);
    Color colorAns = new Color(255, 77, 30);
    Color ovalBlue = new Color(170, 54, 255);

    Graphics gArray;

    //paint
    int arrayX = 70;
    int arrayY = 35;
    int constHeight = 70;
    int diameter = 20;

    //Button & Label
    int count;
    int countButton = 5;
    int wightButton;
    int heightButton;
    Button[] bList = new Button[countButton];

    //run
    boolean stopFlag;
    Thread t = null;
    int speed = 500;
    int drawSpeed = 20;
    Label speed1, delay;
    TextArea graphIn;
    TextField leftField;
    TextField rightField;
    Label answer;
    Label errorLabel;
    boolean errorFlag;

    void scanTree() {
        String str = graphIn.getText();
        InputReader in = new InputReader(str);

        vertexCount = in.nextInt();
        vx = new int[vertexCount];
        vy = new int[vertexCount];
        leftBorder = new int[vertexCount];
        rightBorder = new int[vertexCount];
        parents = new int[vertexCount];
        sqVertex = new int[2 * vertexCount - 1];
        sqDepth = new int[2 * vertexCount - 1];
        firstVisit = new int[vertexCount];
        Tree = new ArrayList[vertexCount];
        colorVertex = new Color[vertexCount];
        colorFirstVisit= new Color[vertexCount];
        colorSqDepth = new Color[2 * vertexCount - 1];
        colorSqVertex = new Color[2 * vertexCount - 1];

        for (int i = 0; i < vertexCount; ++i) {
            colorVertex[i] = ovalBlack;
            colorFirstVisit[i] = ovalBlack;
        }
        for (int i = 0; i < colorSqDepth.length; ++i)  {
            colorSqDepth[i] = ovalBlack;
            colorSqVertex[i] = ovalBlack;
        }
        Arrays.fill(firstVisit, -1);
        Arrays.fill(parents, -1);
        for (int i = 0; i < vertexCount; ++i)
            Tree[i] = new ArrayList<Integer>();

        for (int i = 0; i < vertexCount - 1; ++i) {
            int par = in.nextInt();
            int son = in.nextInt();
            if (par >= vertexCount || son >= vertexCount)
                throw new NoSuchElementException();
            Tree[par].add(son);
        }
        if (in.st.hasMoreTokens())
            throw new NoSuchElementException();
    }


    public void init () {
        setSize(appletWight, appletHigh);
        Label LCA = new Label("Визуализация алгоритма LCA");
        add(LCA);
        errorFlag = false;
    }

    public void start() {
        coordinatesButton();
        repaint();
    }

    public void coordinatesButton() {
        setLayout(null);

        wightButton = 60;
        heightButton = 25;

        Label sqVertexLabel = new Label("sqVertex");
        add(sqVertexLabel);
        Label sqDepthLabel = new Label("sqDepth");
        add(sqDepthLabel);
        Label firstVisitLabel = new Label("firstVisit");
        add(firstVisitLabel);
        speed1 = new Label("Speed: " + new Integer(drawSpeed).toString());
        add(speed1);
        delay = new Label("Delay: " + new Integer(speed).toString());
        add(delay);
        Label graphInLabel1 = new Label("Введите кол-во вершин");
        add(graphInLabel1);
        Label graphInLabel2 = new Label("и ребра дерева: ");
        add(graphInLabel2);
        graphIn = new TextArea(standardGraph);
        add(graphIn);
        Label leftLabel = new Label("Вершина 1");
        add(leftLabel);
        Label rightLabel = new Label("Вершина 2");
        add(rightLabel);
        leftField = new TextField();
        add(leftField);
        rightField = new TextField();
        add(rightField);
        answer = new Label();
        add(answer);
        errorLabel = new Label();
        add(errorLabel);

        add(bList[0] = new Button("Start"));
        add(bList[1] = new Button("Stop"));
        add(bList[2] = new Button("Slower"));
        add(bList[3] = new Button("Faster"));
        add(bList[4] = new Button("LCA"));

        sqVertexLabel.setBounds(arrayX-55, arrayY + 3, 50, 15);
        sqDepthLabel.setBounds(arrayX-55, arrayY+25+3, 50, 15);
        firstVisitLabel.setBounds(arrayX-55, arrayY+50+3, 50, 15);
        bList[0].setBounds(appletWight-150, 30, wightButton, heightButton);
        bList[1].setBounds(appletWight-80, 30, wightButton, heightButton);
        speed1.setBounds(appletWight-150, 60, 150, 20);
        delay.setBounds(appletWight-150, 80, 150, 20);
        bList[2].setBounds(appletWight-150, 105, wightButton, heightButton);
        bList[3].setBounds(appletWight-80, 105, wightButton, heightButton);
        graphInLabel1.setBounds(appletWight-150, 140, 140, 15);
        graphInLabel2.setBounds(appletWight-150, 155, 130, 15);
        graphIn.setBounds(appletWight-150, 175, 130, 200);
        leftLabel.setBounds(appletWight-150, 380, 70, 15);
        rightLabel.setBounds(appletWight-80, 380, 70, 15);
        leftField.setBounds(appletWight-130, 400, 25, 20);
        rightField.setBounds(appletWight-60, 400, 25, 20);
        bList[4].setBounds(appletWight-150, 425, wightButton, heightButton);
        answer.setBounds(appletWight-70, 425, 70, 25);
        errorLabel.setBounds(arrayX-55, appletHigh-30, 300, 15);

        for (int i = 0; i < countButton; i++)
            bList[i].addActionListener(this);
    }


    int v1 = 0, v2 = 0, ans = 0;
    Pair tmp = null;

    public void actionPerformed(ActionEvent e) {

        speed1.setText("Speed: " + new Integer(drawSpeed).toString());
        delay.setText("Delay: " + new Integer(speed).toString());

        if (e.getSource() == bList[0]) {
            if (t != null) t.stop();
            try {
                errorLabel.setText("");
                answer.setText("");
                scanTree();

                count = 0;
                bList[1].setLabel("Stop");
                t = new Thread(this);
                t.start();
            } catch (NoSuchElementException ea) {
                errorLabel.setText("Неправельно введены вершины и ребра дерева!");
            }
        }
        if (e.getSource() == bList[1]) {
            if (bList[1].getLabel() == "Stop") {
                t.suspend();
                bList[1].setLabel("Resume");
            } else {
                bList[1].setLabel("Stop");
                t.resume();
            }
        }
        if (e.getSource() == bList[2]) {
            if (drawSpeed > 0 && speed < 1500) {
                speed += 50;
                drawSpeed -= 1;
                bList[3].setEnabled(true);
            } else {
                bList[2].setEnabled(false);
                drawSpeed = 0;
                speed = 1500;
            }
        }
        if (e.getSource() == bList[3]) {
            if (drawSpeed < 30 && speed > 0) {
                speed -= 50;
                drawSpeed += 1;
                bList[2].setEnabled(true);
            } else {
                bList[3].setEnabled(false);
                drawSpeed = 30;
                speed = 0;
            }
        }
        if (e.getSource() == bList[4]) {

            answer.setText("");

            if (tmp != null) {
                updateLCA(ovalBlack, ovalBlack);
            }

            try {
                errorLabel.setText("");
                v1 = Integer.parseInt(leftField.getText());
                v2 = Integer.parseInt(rightField.getText());


                if (v1 > vertexCount || v2 > vertexCount)
                    throw new NumberFormatException();
                else {
                    vertexOne = firstVisit[v1];
                    vertexTwo = firstVisit[v2];
                    if (vertexOne > vertexTwo) {
                        int swap = vertexTwo;
                        vertexTwo = vertexOne;
                        vertexOne = swap;
                    }
                    tmp = RMQ(1, 0, sqDepth.length-1, vertexOne, vertexTwo);
                    ans = sqVertex[tmp.vertex];

                    answer.setText("LCA = " + new Integer(ans).toString());
                    updateLCA(ovalBlue, colorAns);
                }
            } catch (NumberFormatException ea) {
                errorLabel.setText("Вершины 1 и 2 указаны неверно!");
            } catch (StackOverflowError ea) {
                errorLabel.setText("Дождитесь построение графа!");
            } catch (NullPointerException ea) {
                errorLabel.setText("Дождитесь построение графа!");
            }
        }
    }

    public void updateLCA (Color color, Color colorAns) {
        try {
            colorFirstVisit[v1] = color;
            colorFirstVisit[v2] = color;
            colorVertex[v1] = color;
            colorVertex[v2] = color;
            for (int i = vertexOne; i <= vertexTwo; i++)
                colorSqDepth[i] = color;
            colorVertex[ans] = colorAns;
            colorSqVertex[tmp.vertex] = colorAns;
            repaint();
        } catch (ArrayIndexOutOfBoundsException ea) {
            errorLabel.setText("Вершина указана неверно!");
        }
    }

    public synchronized void run() {
        coordinatesTree(0, 0, appletWight - 200, 0, -1, 0);
        count++;
        repaint();
        int tmp = 1;
        sizeSegmentTree = 2;
        while (tmp < sqDepth.length) {
            tmp *= 2;
            sizeSegmentTree += tmp;
        }
        segmentTree = new Pair[sizeSegmentTree];
        buildTree(sqDepth, 1, 0, sqDepth.length - 1);
    }

    public void paint (Graphics g) {
        if (errorFlag) return;
        //arrays
        this.gArray = g;

        drawArray(sqVertex, 0, colorSqVertex);
        drawArray(sqDepth, 25, colorSqDepth);
        drawArray(firstVisit, 50, colorFirstVisit);

        //tree
        if (vertexCount != 1) {
        g.setColor(line);
        for (int i = 1; i < vertexCount; ++i)
            if (parents[i] != -1)
                g.drawLine(vx[i], vy[i]-diameter/2+arrayY+30, vx[parents[i]], vy[parents[i]]+diameter/2+arrayY+30);
        for (int i = 0; i < vertexCount; ++i)
            if (parents[i] != -1) {
                g.setColor(colorVertex[i]);
                g.fillOval(vx[i]-diameter/2, vy[i]-diameter/2+arrayY+30, diameter, diameter);
                g.setColor(digit);
                if (i < 10)
                    g.drawString(new Integer(i).toString(), vx[i]-3, vy[i]+5+arrayY+30);
                else
                    g.drawString(new Integer(i).toString(), vx[i]-7, vy[i]+5+arrayY+30);
            }
        }
    }

    public void drawArray (int a[], int y, Color color[]) {
        gArray = getGraphics();
        int lengthArray = 1;
        int l = 20;
        int digitL = 15;
        int stepLine = -20;
        if (a != null) lengthArray = a.length;
        gArray.drawRect(arrayX, arrayY+y, lengthArray*l, l);
        if (vertexCount != 1) {
            for (int i = 0; i < lengthArray; i++) {
                stepLine += l;
                if (color[i].equals(ovalBlack)) {
                    gArray.setColor(Color.black);
                    gArray.drawLine(arrayX+stepLine, arrayY+y, arrayX+stepLine, arrayY+y+l);
                    if (a[i] < 10)
                        gArray.drawString(new Integer(a[i]).toString(), arrayX+stepLine+digitL-8, arrayY+y+digitL);
                    else
                        gArray.drawString(new Integer(a[i]).toString(), arrayX+stepLine+digitL-11, arrayY+y+digitL);
                } else {
                    gArray.setColor(color[i]);
                    gArray.fillRect(arrayX+stepLine, arrayY+y, l, l);
                    gArray.setColor(Color.white);
                    if (a[i] < 10)
                        gArray.drawString(new Integer(a[i]).toString(), arrayX+stepLine+digitL-8, arrayY+y+digitL);
                    else
                        gArray.drawString(new Integer(a[i]).toString(), arrayX+stepLine+digitL-11, arrayY+y+digitL);
                }
            }
        }
    }



    public void stop() {
        stopFlag = true;
        t = null;
    }

    public void updatePaint (int v, int step) {
        int updateTmp = count;
        sqVertex[count] = v;
        sqDepth[count] = step;
        ++count;

        colorSqDepth[updateTmp] = ovalGrey;
        colorSqVertex[updateTmp] = ovalGrey;
        colorFirstVisit[v] = ovalGrey;
        colorVertex[v] = ovalGrey;
        repaint();
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) { }
        colorSqDepth[updateTmp] = ovalBlack;
        colorSqVertex[updateTmp] = ovalBlack;
        colorFirstVisit[v] = ovalBlack;
        colorVertex[v] = ovalBlack;

    }

    public void coordinatesTree(int v, int left, int right, int height, int parent, int step) {
        leftBorder[v] = left;
        rightBorder[v] = right;
        parents[v] = parent;
        vy[v] = height + constHeight;
        vx[v] = (left + right) / 2;
        if (v == 0) parents[v] = 0;

        if (firstVisit[v] == -1) firstVisit[v] = count;
        updatePaint(v, step);

        int cntSon = Tree[v].size();
        if (cntSon == 0) return;
        int length = (right - left) / cntSon;
        int cntR = left + length;

        for (int x : Tree[v]) {
            coordinatesTree(x, left, cntR, height + constHeight, v, step + 1);

            updatePaint(v, step);

            cntR += length;
            left += length;
        }
    }

    public void buildTree (int a[], int v, int tl, int tr) {
        if (tl == tr)
            segmentTree[v] = new Pair(a[tl], tl);
        else {
            int tm = (tl + tr) / 2;
            buildTree (a, v*2, tl, tm);
            buildTree (a, v*2+1, tm+1, tr);
            if (segmentTree[v*2].vel < segmentTree[v*2+1].vel)
                segmentTree[v]= new Pair(segmentTree[v*2].vel, segmentTree[v*2].vertex);
            else
                segmentTree[v] = new Pair(segmentTree[v*2+1].vel, segmentTree[v*2+1].vertex);
        }
    }

    public Pair RMQ (int v, int tl, int tr, int l, int r) {
        if (l > r) return new Pair(Integer.MAX_VALUE, 1);
        if (l == tl && r == tr) return segmentTree[v];
        int tm = (tl + tr) / 2;
        Pair v1 = RMQ(v*2, tl, tm, l, Math.min(r,tm));
        Pair v2 = RMQ(v*2+1, tm+1, tr, Math.max(l, tm+1), r);
        if (v1.vel < v2.vel) return v1;
        else return v2;
    }

    class Pair {
        int vel, vertex;
        Pair(int vel, int vertex){
            this.vel = vel;
            this.vertex = vertex;
        }
    }
}