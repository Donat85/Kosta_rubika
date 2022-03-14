package tutorial3D;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;





public class Kostka extends JFrame implements ActionListener{

	
	
	ColorCube kostka[];
	Transform3D kostka_p[];
	Transform3D rotX90;
	Transform3D rotXm90;
	Transform3D rotY90;
	Transform3D rotYm90;
	Transform3D rotZ90;
	Transform3D rotZm90;
	
	
	
	Text3D text;
	Vector<String> kostka_ruchy[];
	
	TransformGroup pozycjaKostki[];
	TransformGroup text_g;
	
	//Appearance wyglad_kostki;
	Appearance wyglad_kostki_podswietlonej;
	Material podswietlony_material;
	
	TransformGroup grupakostek;
	Canvas3D canvas3D;
	boolean klawisz=true;
	Vector3f osruchu;
	SimpleUniverse simpleU;
	int obecnepodswietlenie=-1;
	
	
    Kostka(){

    	//stworzenie wirtualnego swiata

        super("Kosteczka");
        
        setResizable(false);

        
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(800,600));
        
        canvas3D.setFocusable(false);
        
        add(canvas3D);
        pack();
        setVisible(true);
        requestFocus();
        setFocusable(true);
        
        BranchGroup scena = utworzScene();
	    scena.compile();

       
		simpleU = new SimpleUniverse(canvas3D);
        
        

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.0f,21.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        //przemieszczanie obserwatora za pomoca myszki
		OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ROTATE);
		orbit.setSchedulingBounds(new BoundingSphere());
		simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);

        simpleU.addBranchGraph(scena);
        
    }

      BranchGroup utworzScene(){

      BranchGroup main_branch = new BranchGroup();
	
      //swiatlo
      
      BoundingSphere bounds = new BoundingSphere(new Point3d(0.0d,0.0d,0.0d),30d);

      AmbientLight lightA = new AmbientLight();
      lightA.setInfluencingBounds(bounds);
      main_branch.addChild(lightA);

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
      lightD.setColor(new Color3f(1.0f, 1.0f, 1.0f));
      main_branch.addChild(lightD);

      //grupakostek  TransformGroup zawieraj¹cy wszystkie mniejsze kostki
      
      TransformGroup grupakostek = new TransformGroup();
      grupakostek.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      grupakostek.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
      
      //Transformacje obrotów, potrzebne do wykonywania ruchów
      
      rotX90 = new Transform3D();
      rotX90.rotX(Math.PI/2);
      rotXm90 = new Transform3D();
      rotXm90.rotX(Math.PI/-2);
      rotY90 = new Transform3D();
      rotY90.rotY(Math.PI/2);
      rotYm90 = new Transform3D();
      rotYm90.rotY(Math.PI/-2);
      rotZ90 = new Transform3D();
      rotZ90.rotZ(Math.PI/2);
      rotZm90 = new Transform3D();
      rotZm90.rotZ(Math.PI/-2);   
      
      //osruchu- wektor, który wskazuje któr¹ œcian¹ program ma poruszyæ
      
      osruchu = new Vector3f((float)-2.1,(float)2.1,(float)2.1);
      
      //stworzenie kostki

      createCubes(grupakostek);
      

      // ActionListener
      this.addKeyListener(new KeyListener(){

          public void keyPressed(KeyEvent e){
              switch(e.getKeyCode()){
              case KeyEvent.VK_W:       if(klawisz)ruch("xm"); break;
              case KeyEvent.VK_S:  		if(klawisz)ruch("xp"); break;
              case KeyEvent.VK_A:  	    if(klawisz)ruch("ym"); break;
              case KeyEvent.VK_D:  	    if(klawisz)ruch("yp"); break;
              case KeyEvent.VK_Q:       if(klawisz)ruch("zp"); break;
              case KeyEvent.VK_E:       if(klawisz)ruch("zm"); break;
			  case KeyEvent.VK_UP:      if(klawisz)zmianaosi(1); break;
			  case KeyEvent.VK_DOWN:    if(klawisz)zmianaosi(-1); break;
			  case KeyEvent.VK_LEFT:    if(klawisz)zmianaosi(2); break;
			  case KeyEvent.VK_RIGHT:   if(klawisz)zmianaosi(-2); break;
                  
              }
              
          }

          public void keyReleased(KeyEvent e){
          klawisz=true;
          podswietl();
          }
          

		public void keyTyped(KeyEvent e) {
		}
      });

      this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {

			}
			
			public void mousePressed(MouseEvent e) {
			}
			
			public void mouseReleased(MouseEvent e) {
			}
			
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}
      });

      canvas3D.addKeyListener(new KeyListener(){

          public void keyPressed(KeyEvent e){
              switch(e.getKeyCode()){
                  case KeyEvent.VK_W:       if(klawisz)ruch("xm"); break;
                  case KeyEvent.VK_S:    	if(klawisz)ruch("xp"); break;
                  case KeyEvent.VK_A:       if(klawisz)ruch("ym"); break;
                  case KeyEvent.VK_D:   	if(klawisz)ruch("yp"); break;
                  case KeyEvent.VK_Q:       if(klawisz)ruch("zp"); break;
                  case KeyEvent.VK_E:       if(klawisz)ruch("zm"); break;
				  case KeyEvent.VK_UP:      if(klawisz)zmianaosi(1); break;
				  case KeyEvent.VK_DOWN:    if(klawisz)zmianaosi(-1); break;
				  case KeyEvent.VK_LEFT:    if(klawisz)zmianaosi(2); break;
				  case KeyEvent.VK_RIGHT:   if(klawisz)zmianaosi(-2); break;
                  
              }
          }

          public void keyReleased(KeyEvent e){
        	  
        	  klawisz=true;
        	  podswietl();
          }

		public void keyTyped(KeyEvent e) {
		}
      });
      canvas3D.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
			}
			
			public void mouseReleased(MouseEvent e) {
				setobserwator();
				osPoObrocie();
				podswietl();
			}
			
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}
      });

      
      // dodanie kostek do ga³êzi
      main_branch.addChild(grupakostek);
      

      return main_branch;

    }

   public static void main(String args[]){
    
      
   }


	public void actionPerformed(ActionEvent e) {
	}
	
	public char obserwator_polozenie(Vector3f obserwator) { // funkcja zwraca wersor, którego wartoœæ bezwzglêdna jest najwiêksza -> pozycja naszej kamery
		float x = Math.abs(obserwator.x);
		float y = Math.abs(obserwator.y);
		float z = Math.abs(obserwator.z);
		
		if(x>y && x>z) {
			return 'x';
		}
		if(y>x && y>z) {
			return 'y';
		}
		if(z>y && z>x) {
			return 'z';
		}
		return 0;
	}

	public void ruch(String a){  // funkcja wykonuje odpowiedni ruch, parametr a mówi nam jaki ruch chemy wykonaæ np. xm oznacza ze chcemy wykonaæ ruch wokó³ osi X o k¹t ujemny(minus), xp to samo tylko k¹t dodatni(plus)
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie); // odczytujemy po³o¿enie kamery
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);


		// w zale¿noœci od pozycji kamery wykonuj¹ siê odpowiednie ruchy takie, ¿eby ruch przyciskiem W robi³ ruch "do góry" z perspektywy obecnej kamery, inne przyciski odpowiednie ruchy dla obecnej pozycji kamery
		if(widok=='x' && pozycja.x>0)
		{
			osruchu.x=(float)2.1; // kostka, która zawiera siê we wszystkich œcianach, którymi mo¿emy w danym momencie poruszyæ zawsze znajduje siê na œcianie naprzeciwko kamery

			// w podstawowej wersji(kamera na osi Z, oœ Y do góry, oœ X w prawo): w - xm, s - xp, a - ym, d - yp, q - zp, e - zm

			if(a=="xp")
				pressedS();
			if(a=="xm")
				pressedW();
			if(a=="yp")
				pressedD();
			if(a=="ym")
				pressedA();
			if(a=="zp")
				pressedQ();
			if(a=="zm")
				pressedE();

		}
		if(widok=='x' && pozycja.x<0)
		{
			osruchu.x=(float)-2.1;

			if(a=="xm")
				pressedS();
			if(a=="xp")
				pressedW();
			if(a=="ym")
				pressedA();
			if(a=="yp")
				pressedD();
			if(a=="zm")
				pressedQ();
			if(a=="zp")
				pressedE();
		}
		if(widok=='y' && pozycja.y>0)
		{
			osruchu.y=(float)2.1;
			if(a=="xp")
				pressedS();
			if(a=="xm")
				pressedW();
			if(a=="yp")
				pressedD();
			if(a=="ym")
				pressedA();
			if(a=="zp")
				pressedQ();
			if(a=="zm")
				pressedE();

		}
		if(widok=='y' && pozycja.y<0)
		{
			osruchu.y=(float)-2.1;
			if(a=="xm")
				pressedS();
			if(a=="xp")
				pressedW();
			if(a=="ym")
				pressedA();
			if(a=="yp")
				pressedD();
			if(a=="zm")
				pressedQ();
			if(a=="zp")
				pressedE();
		}
		if(widok=='z' && pozycja.z>0)
		{
			osruchu.z=(float)2.1;

			if(a=="xp")
				pressedS();
			if(a=="xm")
				pressedW();
			if(a=="yp")
				pressedD();
			if(a=="ym")
				pressedA();
			if(a=="zp")
				pressedQ();
			if(a=="zm")
				pressedE();
		}
		if(widok=='z' && pozycja.z<0)
		{
			osruchu.z=(float)-2.1;

			if(a=="xm")
				pressedS();
			if(a=="xp")
				pressedW();
			if(a=="ym")
				pressedA();
			if(a=="yp")
				pressedD();
			if(a=="zm")
				pressedQ();
			if(a=="zp")
				pressedE();
		}
	}
	// funkcje pressed WSADQE wykonuj¹ odpowiednie ruchy, wszystkie funkcje dzia³aj¹ tak samo, opis funkcji znajduje sie w komentarzu w funkcji pressedQ
	public void pressedS() {
		
		klawisz=false;
		
		
		Vector3f v3f = new Vector3f();
		Vector3f v3fbuf = new Vector3f();
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);
		
		
		
		if(widok=='z'||widok=='y') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.x==osruchu.x) {
	
					v3f.x= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * -1.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("xp");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--) {
						addTransform3D(kostka_ruchy[i].get(j), buf);
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
		else {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.z==osruchu.z) {

					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * -1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("zm");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
	}
	
	public void pressedW() {
		klawisz=false;
		
		Vector3f v3f = new Vector3f();
		Vector3f v3fbuf = new Vector3f();
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);
		
		if(widok=='y' || widok=='z') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.x==osruchu.x) {
	
					v3f.x= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * -1.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("xm");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
		else {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.z==osruchu.z) {

					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * -1.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("zp");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
	}
	
	public void pressedD() {
		klawisz=false;
		
		
		
		Vector3f v3f = new Vector3f();
		Vector3f v3fbuf = new Vector3f();
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);
		
		if(widok=='z'||widok=='x') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.y==osruchu.y) {
	
					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * -1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("yp");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
		else {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.z==osruchu.z) {

					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * -1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("zm");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
	}
	
	public void pressedA() {
		klawisz=false;
		
		
		
		Vector3f v3f = new Vector3f();
		Vector3f v3fbuf = new Vector3f();
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);
		
		if(widok=='z'||widok=='x') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.y==osruchu.y) {
	
					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * -1.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("ym");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
		else {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.z==osruchu.z) {

					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * -1.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("zp");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
	}
		
	public void pressedQ() {
		klawisz=false;
		
		
		
		Vector3f v3f = new Vector3f();
		Vector3f v3fbuf = new Vector3f();
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja); 
		
		if(widok=='z') {   // w zale¿noœci od pozycji kamery obracana kostka jest o ró¿ne osie
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.z==osruchu.z) {
	
					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * -1.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0); //macierz rotacji aby przemieœcic mniejsz¹ kostkê na now¹ pozycje po wykonaniu ruchu
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("zp");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {  //kostka ruchy zawiera poprzednio wykonane obroty danej kostki, dziêki ich z³¹czeniu funkcj¹ mul, otrzymujemy j¹ dobrze obrócon¹ po wykonaniu ruchu
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					
					kostka_p[i].get(v3fbuf);
					
					
				}
			}
		}
		if(widok=='y') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.y==osruchu.y) {
	
					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * -1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("yp");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
		if(widok=='x') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.x==osruchu.x) {
	
					v3f.x= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * -1.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("xp");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--) {
						addTransform3D(kostka_ruchy[i].get(j), buf);
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
	}
	
	public void pressedE() {
		klawisz=false;
		
		
		
		Vector3f v3f = new Vector3f();
		Vector3f v3fbuf = new Vector3f();
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);
		
		if(widok=='z') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.z==osruchu.z) {
	
					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * -1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("zm");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
		if(widok=='y') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.y==osruchu.y) {
	
					v3f.x= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * -1.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 1.0) + (float)(v3fbuf.z * 0.0);
					v3f.z= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("ym");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
						System.out.println(kostka_ruchy[i].get(j));
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
		if(widok=='x') {
			for(int i=0;i<27;i++) {
				
				kostka_p[i].get(v3f);
				kostka_p[i].get(v3fbuf);
				if(v3f.x==osruchu.x) {
	
					v3f.x= (float) (v3fbuf.x * 1.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 0.0);
					v3f.y= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * 0.0) + (float)(v3fbuf.z * 1.0);
					v3f.z= (float) (v3fbuf.x * 0.0) + (float)(v3fbuf.y * -1.0) + (float)(v3fbuf.z * 0.0);
					kostka_p[i].set(v3f);
					
					Transform3D buf = new Transform3D();
					buf.mul(kostka_p[i]);
					kostka_ruchy[i].add("xm");
					clearTransform3D(kostka_ruchy[i]);
					for(int j=kostka_ruchy[i].size()-1;j>=0;j--)  {
						addTransform3D(kostka_ruchy[i].get(j), buf);
					}
					
					
					pozycjaKostki[i].setTransform(buf);
					System.out.println(v3fbuf);
					kostka_p[i].get(v3fbuf);
					System.out.println(v3fbuf);
					
				}
			}
		}
	}
	
	public void addTransform3D(String axis, Transform3D buf) { // w zale¿noœci co otrzyma jako argument, funkcja wykonuje obrót o + lub - 90 wokó³ odpowiedniej osi
		switch(axis) {
		case "xm":  buf.mul(rotXm90); break;
		case "xp":  buf.mul(rotX90); break;
		case "ym":  buf.mul(rotYm90); break;
		case "yp":  buf.mul(rotY90); break;
		case "zm":	buf.mul(rotZm90); break;
		case "zp":	buf.mul(rotZ90); break;
		}
	}
	
	public void clearTransform3D(Vector<String> ruchy) { // funkcja usuwa elementy z vektora poprzednich transformacji, np po wykonaniu 4 obrotów o 90 wokó³ osi x, wracamy do pkt wyjœcia, wiêc czyszczony jest wektor z niepotrzebnych transformacji, zabieg ma na celu zredukowanie zu¿ywanej pamiêci przez program oraz,aby wraz w wykonywaniem kolejnych ruchów nie stawa³ siê coraz wolniejszy
		String[] szukany = new String[6];
		
		szukany[0]="xm";
		szukany[1]="xp";
		szukany[2]="yp";
		szukany[3]="ym";
		szukany[4]="zm";
		szukany[5]="zp";
		for(int j=0;j<6;j++) {
			int ilosc =0;
			int size = ruchy.size();
			for(int i=0;i<size;i++) {
				if(ruchy.get(i)==szukany[j]) {
					ilosc++;
				}
			}
			if(ilosc==4) {
				for(int i=0;i<4;i++) {
					ruchy.removeElement(szukany[j]);
				}
			}
		}
	}
	
	public void zmianaosi(int a) { // funkcja pozwala na wybranie klocka (a tym samym œciany), którym chcemy poruszyæ

		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);



		if(widok=='x' && pozycja.x>0) {
			if (a == 1 && osruchu.y != 2.1f) osruchu.y = osruchu.y + 2.1f;
			if (a == -1 && osruchu.y != -2.1f) osruchu.y = osruchu.y - 2.1f;
			if (a == 2 && osruchu.z != 2.1f) osruchu.z = osruchu.z + 2.1f;
			if (a == -2 && osruchu.z != -2.1f) osruchu.z = osruchu.z - 2.1f;
		}
		if(widok=='x' && pozycja.x<0) {
			if (a == 1 && osruchu.y != 2.1f) osruchu.y = osruchu.y + 2.1f;
			if (a == -1 && osruchu.y != -2.1f) osruchu.y = osruchu.y - 2.1f;
			if (a == 2 && osruchu.z != -2.1f) osruchu.z = osruchu.z - 2.1f;
			if (a == -2 && osruchu.z != 2.1f) osruchu.z = osruchu.z + 2.1f;
		}
		if(widok=='y' && pozycja.y>0) {
			if (a == 1 && osruchu.z != -2.1f) osruchu.z = osruchu.z - 2.1f;
			if (a == -1 && osruchu.z != 2.1f) osruchu.z = osruchu.z + 2.1f;
			if (a == 2 && osruchu.x != -2.1f) osruchu.x = osruchu.x - 2.1f;
			if (a == -2 && osruchu.x != 2.1f) osruchu.x = osruchu.x + 2.1f;
		}
		if(widok=='y' && pozycja.y<0) {
			if (a == 1 && osruchu.z != -2.1f) osruchu.z = osruchu.z - 2.1f;
			if (a == -1 && osruchu.z != 2.1f) osruchu.z = osruchu.z + 2.1f;
			if (a == 2 && osruchu.x != 2.1f) osruchu.x = osruchu.x + 2.1f;
			if (a == -2 && osruchu.x != -2.1f) osruchu.x = osruchu.x - 2.1f;
		}
		if(widok=='z' && pozycja.z>0) {
			if (a == 1 && osruchu.y != 2.1f) osruchu.y = osruchu.y + 2.1f;
			if (a == -1 && osruchu.y != -2.1f) osruchu.y = osruchu.y - 2.1f;
			if (a == 2 && osruchu.x != -2.1f) osruchu.x = osruchu.x - 2.1f;
			if (a == -2 && osruchu.x != 2.1f) osruchu.x = osruchu.x + 2.1f;
		}
		if(widok=='z' && pozycja.z<0) {
			if (a == 1 && osruchu.y != 2.1f) osruchu.y = osruchu.y + 2.1f; //gora
			if (a == -1 && osruchu.y != -2.1f) osruchu.y = osruchu.y - 2.1f; //dol
			if (a == 2 && osruchu.x != 2.1f) osruchu.x = osruchu.x + 2.1f;  //lewo
			if (a == -2 && osruchu.x != -2.1f) osruchu.x = osruchu.x - 2.1f;  //prawo
		}


		
	}
	
	public void setobserwator() { // po wykonaniu obrotu kamer¹ ta funkcja lekko poprawia po³o¿enie kamery, aby nasz obserwator mia³ "u góry" rosn¹ce wartoœci osi y (jesli znajduje siê bardzo blisko osi y to "u góry ma ujemne wartoœci z)
									// informacja o po³o¿eniu kamery nie jest wystarczaj¹ca do wykonania prawid³owego obrotu œcian¹ kostki
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);

		char widok = obserwator_polozenie(pozycja);
		if(widok=='y')
			przesuniecie.lookAt(new Point3d((double)pozycja.x,(double)pozycja.y,(double)pozycja.z), new Point3d(0.0d,0.0d,0.0d), new Vector3d(0.0d,0.0d,-1.0d));
		else
			przesuniecie.lookAt(new Point3d((double)pozycja.x,(double)pozycja.y,(double)pozycja.z), new Point3d(0.0d,0.0d,0.0d), new Vector3d(0.0d,1.0d,0.0d));


		przesuniecie.invert();
		
		
		simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie);
		
		
		
		
	}

	public void podswietl() { // funkcja która zaznacza kostkê wokó³, której wykonywany jest ruch
		
		Vector3f buffer = new Vector3f();
		
		
		if(obecnepodswietlenie>-1) {
			kostka[obecnepodswietlenie]= new ColorCube();
		}
		
		for(int i =0;i<27;i++) {
			kostka_p[i].get(buffer);
			if(buffer.x==osruchu.x && buffer.y==osruchu.y && buffer.z==osruchu.z ) {
				
				kostka[i] = new ColorCube(0.2d);
				obecnepodswietlenie=i;
			}
		}
		
		
		
	}
	
	public void osPoObrocie() { // funkcja ustawia os ruchu, tak abyœmy wykonywali ruchy na œcianie, na któr¹ patrzymy
		
		Transform3D przesuniecie = new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(przesuniecie);
		Vector3f pozycja = new Vector3f();
		przesuniecie.get(pozycja);
		char widok = obserwator_polozenie(pozycja);
		
		if(widok=='x' && pozycja.x>0) {
			osruchu.x=(float) 2.1;
		}
		if(widok=='x' && pozycja.x<0) {
			osruchu.x=(float) -2.1;
		}
		if(widok=='y' && pozycja.y>0) {
			osruchu.y=(float) 2.1;
		}
		if(widok=='y' && pozycja.y<0) {
			osruchu.y=(float) -2.1;
		}
		if(widok=='z' && pozycja.z>0) {
			osruchu.z=(float) 2.1;
		}
		if(widok=='z' && pozycja.z<0) {
			osruchu.z=(float) -2.1;
		}
	}
	
	public void createCubes(TransformGroup grupakostek) {	//funkcja tworzy kostki i ustawia je w odpowiednich miejscach
		
		
		  kostka = new ColorCube[27];
	      kostka_p = new Transform3D[27];

	      kostka_ruchy = new Vector[27];

	      pozycjaKostki = new TransformGroup[27];
	      
	      for(int i=0; i<27;i++) {
	    	  
	    	  
	    	  
	    	  kostka_ruchy[i] = new Vector<String>();
	    	  
	      }
	      int z=0;
	      for(double i=-2.1; i<=2.1; i+=2.1) {
	          for(double j=-2.1; j<=2.1; j+=2.1) {
	             for(double k=-2.1; k<=2.1; k+=2.1) {
	                kostka[z] = new ColorCube();
	                kostka[z].setCapability(ColorCube.ALLOW_APPEARANCE_WRITE);
	                kostka[z].setAppearanceOverrideEnable(true);
	                
	                kostka_p[z] = new Transform3D();
	                kostka_p[z].set(new Vector3f((float)i, (float)j, (float)k));
	                pozycjaKostki[z] = new TransformGroup(kostka_p[z]);
	                pozycjaKostki[z].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	                pozycjaKostki[z].addChild(kostka[z]);
	              
	                grupakostek.addChild(pozycjaKostki[z]);
	                z++;
	             }
	          }
	       }
  
	}
	
	public void resetuj() { // funkcja "restartuje" kostkê

		 int z=0;
	      for(double i=-2.1; i<=2.1; i+=2.1) {
	          for(double j=-2.1; j<=2.1; j+=2.1) {
	             for(double k=-2.1; k<=2.1; k+=2.1) {
	            	
	                kostka_p[z].set(new Vector3f((float)i, (float)j, (float)k));
	                kostka_ruchy[z].clear();
	                
	                pozycjaKostki[z].setTransform(kostka_p[z]);
	                
	                z++;
	             }
	          }
	       }
	      	Transform3D przesuniecie2 = new Transform3D();
			przesuniecie2.lookAt(new Point3d(0.0d,0.0d,21.0d), new Point3d(0.0d,0.0d,0.0d), new Vector3d(0.0d,1.0d,0.0d));
			przesuniecie2.invert();
			
			
			simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie2);
	}
}

	




