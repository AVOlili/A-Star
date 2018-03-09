import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("rawtypes")
public class EightFigure2 implements Comparable{
    private int[] num = new int[9];
    //这里的g为深度，h为各棋子不在正确位置的数目
    //当然h也可以设置为个棋子与目标位置的距离，但是太麻烦
    private int g;                    //当前的深度即走到当前状态的步骤
    private int f;                //从起始状态到目标的最小估计值
    private int h;            //到目标的最小估计
    private EightFigure2 parent;            //当前状态的父状态
    public int[] getNum() {
        return num;
    }
    public void setNum(int[] num) {
        this.num = num;
    }
    public int getg() {
        return g;
    }
    public void setg(int g) {
        this.g = g;
        System.out.print("g(n)="+g+" ");
    }
    public int getf() {
        return f;
    }
    public void setf(int f) {
        this.f = f;
        System.out.print("f(n)="+f+" ");
    }
    public int geth() {
        return h;
    }
    public void seth(int h) {
        this.h = h;
        System.out.print("h(n)="+h+"  ");
    }
    public EightFigure2 getParent() {
        return parent;
    }
    public void setParent(EightFigure2 parent) {
        this.parent = parent;
    }
    /**
     * 判断当前状态是否为目标状态
     * @param target
     * @return
     */
    public boolean isTarget(EightFigure2 target){
        return Arrays.equals(this.getNum(), target.getNum());
    }

    /**
     * 求f(n) = g(n)+h(n);
     * 初始化状态信息
     * @param target
     */
    public void init(EightFigure2 target){
        int sum = 0;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(target.num[i]==this.num[j]){
                    int x = 0;
                    x=Math.abs(j-i);
                    sum += Math.pow(x,1.0/4);
                }
            }
        }
        seth(sum);

        if(this.getParent()==null){
            this.setg(0);//如果没有父节点
        }else{
            this.g = this.parent.getg()+1;
            setg(g);
        }
        this.setf(this.getg()+this.geth());
        System.out.println();
    }
    /**
     * 数学问题如果初始状态和目标状态的逆序值同为奇数或同为偶数
     * 则可以通过有限次数的移动到达目标状态，否则无解。
     * @TODO求逆序值并判断是否有解
     * @param target
     * @return 有解：true 无解：false
     */
    public boolean isSolvable(EightFigure2 target){
        int reverse = 0;
        for(int i=0;i<9;i++){
            for(int j=0;j<i;j++){
                //这里的this就是start，因为用了start.isSolvable(target)
                if(this.num[j]>this.num[i])
                    reverse++;
                if(target.getNum()[j]>target.getNum()[i])
                    reverse++;
            }
        }
        if(reverse % 2 == 0)
            return true;
        return false;
    }
    @Override
    public int compareTo(Object o) {
        EightFigure2 t = (EightFigure2) o;
        return this.f-t.getf();//默认排序为f(n)由小到大排序
    }
    /**
     * @return 返回0在八数码中的位置
     */
    public int getZeroPosition(){
        int position = -1;
        for(int i=0;i<9;i++){
            if(this.num[i] == 0){
                position = i;
            }
        }
        return position;
    }
    /**
     *
     * @param open    状态集合
     * @return 判断当前状态是否存在于open表中
     */
    public int isContains(ArrayList<EightFigure2> open){
        for(int i=0;i<open.size();i++){
            if(Arrays.equals(open.get(i).getNum(), getNum())){
                return i;
            }
        }
        return -1;
    }
    /**
     *
     * @return 小于3的不能上移返回false
     */
    public boolean isMoveUp() {
        int position = getZeroPosition();
        if(position<=2){
            return false;
        }
        return true;
    }
    /**
     *
     * @return 大于6返回false
     */
    public boolean isMoveDown() {
        int position = getZeroPosition();
        if(position>=6){
            return false;
        }
        return true;
    }
    /**
     * 是否可以左移，如果空格(0)的位置在
     * 数组的0，3，6就不行，及在最左边一排
     * @return 0，3，6返回false
     */
    public boolean isMoveLeft() {
        int position = getZeroPosition();
        if(position%3 == 0){
            return false;
        }
        return true;
    }
    /**
     *
     * @return 2，5，8不能右移返回false
     */
    public boolean isMoveRight() {
        int position = getZeroPosition();
        if((position)%3 == 2){
            return false;
        }
        return true;
    }
    /**
     * @param move 0：上，1：下，2：左，3：右
     * @return 返回移动后的状态
     */
    public EightFigure2 Move(int choice){
        EightFigure2 temp = new EightFigure2();
        int[] tempnum = (int[])num.clone();
        temp.setNum(tempnum);
        int position = getZeroPosition();    //0的位置
        int p=0;                            //与0换位置的位置
        switch(choice){
            //0：上，1：下，2：左，3：右:
            case 0:
                p = position-3;
                temp.getNum()[position] = num[p];
                break;
            case 1:
                p = position+3;
                temp.getNum()[position] = num[p];
                break;
            case 2:
                p = position-1;
                temp.getNum()[position] = num[p];
                break;
            case 3:
                p = position+1;
                temp.getNum()[position] = num[p];
                break;
        }
        temp.getNum()[p] = 0;
        return temp;
    }
    /**
     * 按照八数码的格式输出
     */
    public void print(){
        for(int i=0;i<9;i++){
            if(i%3 == 2){
                System.out.println(this.num[i]);
            }else{
                System.out.print(this.num[i]+"  ");
            }
        }
    }
    /**
     * 反序列的输出状态
     */
    public void printRoute(){
        System.out.println("因为是反序列输出，请从下往上看");
        EightFigure2 temp = null;
        int count = 0;
        temp = this;
        while(temp!=null){
            temp.print();
            System.out.println("----------------------");
            temp = temp.getParent();
            count++;
        }
        System.out.println("生成节点数："+(count-1));
    }
    /**
     *
     * @param open open表
     * @param close close表
     * @param parent 父状态
     * @param target 目标状态
     */
    public void operation(ArrayList<EightFigure2> open, ArrayList<EightFigure2> close, EightFigure2 parent, EightFigure2 target){
        if(this.isContains(close) == -1){
            int position = this.isContains(open);
            if(position == -1){
                this.parent = parent;
                this.init(target);
                open.add(this);
            }else{
                if(this.getg() < open.get(position).getg()){
                    open.remove(position);
                    this.parent = parent;
                    this.init(target);
                    open.add(this);
                }
            }
        }
    }


    public static void main(String args[]){
        //定义open表
        ArrayList<EightFigure2> open = new ArrayList<EightFigure2>();
        ArrayList<EightFigure2> close = new ArrayList<EightFigure2>();
        //创建初始节点和目标节点，这里面包含棋子顺序,f,g,h，指针···属性信息
        EightFigure2 start = new EightFigure2();
        EightFigure2 target = new EightFigure2();

        String lineContent = null;
//       int startnum[] = {1,2,3,8,0,4,7,6,5};
//       int targetnum[] = {2,1,6,4,0,8,7,5,3};
        int startnum[] = new int[9];
        int targetnum[] = new int[9];
        int order = 0;
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader("input.txt") );
            while((lineContent=br.readLine())!=null){//读取一行
                String[] str = lineContent.split(",");
                //System.out.println("input.txt每一行个数"+str.length);
                for(int i = 0 ;i<str.length;i++){
                    if(order==0)
                        startnum[i] = Integer.parseInt(str[i]);
                    else
                        targetnum[i] = Integer.parseInt(str[i]);
                }
                order++;
            }
        } catch (NumberFormatException e) {
            System.out.println("请检查输入文件的格式，例如：2,1,6,4,0,8,7,5,3 换行 1,2,3,8,0,4,7,6,5");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("当前目录下无input.txt文件。");
            e.printStackTrace();
        }
        start.setNum(startnum);
        target.setNum(targetnum);
        long startTime=System.currentTimeMillis();   //获取开始时间
        if(start.isSolvable(target)){
            //初始化初始状态
            start.init(target);
            open.add(start);
            while(open.isEmpty() == false){
                Collections.sort(open);            //会按照f的值从小到大排序，因为重写了compareTo()
                EightFigure2 best = open.get(0);    //从open表中取出最小估值的状态并移除open表
                open.remove(0);
                close.add(best);
                if(best.isTarget(target)){
                    //输出
                    best.printRoute();
                    long endTime=System.currentTimeMillis(); //获取结束时间
                    System.out.println("拓展节点数："+close.size());
                    System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
                    System.out.println(("转成分钟为："+(endTime-startTime)/60000.0)+"min");
                    System.exit(0);
                }
                //best不是目标节点，继续：
                int choice;
                //由best状态进行扩展并加入到open表中
                //0的位置上移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                if(best.isMoveUp()){
                    choice = 0;
                    EightFigure2 temp = best.Move(choice);
                    temp.operation(open, close, best, target);
                }
                //0的位置下移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                if(best.isMoveDown()){
                    choice = 1;
                    EightFigure2 temp = best.Move(choice);
                    temp.operation(open, close, best, target);
                }
                //0的位置左移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                if(best.isMoveLeft()){
                    choice = 2;
                    EightFigure2 temp = best.Move(choice);
                    temp.operation(open, close, best, target);
                }
                //0的位置右移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                if(best.isMoveRight()){
                    choice = 3;
                    EightFigure2 temp = best.Move(choice);
                    temp.operation(open, close, best, target);
                }
            }
        }else
            System.out.println("没有解，请重新输入。");
    }
}
