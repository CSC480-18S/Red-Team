package com.csc480red.scoresdb;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Score {

	@Id
	@JsonIgnore
	private Long id;
	
	private int a;
	private int b;
	private int c;
	private int d;
	private int e;
	private int f;
	private int g;
	private int h;
	private int i;
	private int j;
	private int k;
	private int l;
	private int m;
	private int n;
	private int o;
	private int p;
	private int q;
	private int r;
	private int s;
	private int t;
	private int u;
	private int v;
	private int w;
	private int x;
	private int y;
	private int z;
	
	protected Score() {
		
	}
	
	public Score(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, int k, int l, int m, int n,
			int o, int p, int q, int r, int s, int t, int u, int v, int w, int x, int y, int z) {
		this.id = 0L;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
		this.m = m;
		this.n = n;
		this.o = o;
		this.p = p;
		this.q = q;
		this.r = r;
		this.s = s;
		this.t = t;
		this.u = u;
		this.v = v;
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Long getId() {
		return id;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public int getD() {
		return d;
	}

	public int getE() {
		return e;
	}

	public int getF() {
		return f;
	}

	public int getG() {
		return g;
	}

	public int getH() {
		return h;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public int getK() {
		return k;
	}

	public int getL() {
		return l;
	}

	public int getM() {
		return m;
	}

	public int getN() {
		return n;
	}

	public int getO() {
		return o;
	}

	public int getP() {
		return p;
	}

	public int getQ() {
		return q;
	}

	public int getR() {
		return r;
	}

	public int getS() {
		return s;
	}

	public int getT() {
		return t;
	}

	public int getU() {
		return u;
	}

	public int getV() {
		return v;
	}

	public int getW() {
		return w;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	
	
}
