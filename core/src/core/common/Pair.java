package core.common;

public class Pair<L, R> {

    public final L fst;
    public final R snd;

    public Pair(L left, R right) {
        assert left != null;
        assert right != null;

        this.fst = left;
        this.snd = right;
    }

    public L getFst() {
        return fst;
    }

    public R getSnd() {
        return snd;
    }

    @Override
    public int hashCode() {
        return fst.hashCode() ^ snd.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair))
            return false;
        Pair pairo = (Pair) o;
        return this.fst.equals(pairo.getFst()) &&
                this.snd.equals(pairo.getSnd());
    }
}
