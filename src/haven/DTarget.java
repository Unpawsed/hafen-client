/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

public interface DTarget {
    public boolean drop(Coord cc, Coord ul);
    public boolean iteminteract(Coord cc, Coord ul);

    public default boolean drop(Drop ev) {
	return(drop(ev.c, ev.c.sub(ev.src.doff)));
    }
    public default boolean iteminteract(Interact ev) {
	return(iteminteract(ev.c, ev.c.sub(ev.src.doff)));
    }

    public abstract static class ItemEvent extends Widget.MouseEvent {
	public final ItemDrag src;

	public ItemEvent(Coord c, ItemDrag src) {
	    super(c);
	    this.src = src;
	}
	public ItemEvent(ItemEvent from, Coord c) {
	    super(from, c);
	    this.src = from.src;
	}
    }

    public static class Drop extends ItemEvent {
	public Drop(Coord c, ItemDrag src) {super(c, src);}
	public Drop(Drop from, Coord c) {super(from, c);}
	public Drop derive(Coord c) {return(new Drop(this, c));}

	protected boolean shandle(Widget w) {
	    if((w != src) && (w instanceof DTarget) && ((DTarget)w).drop(this))
		return(true);
	    return(super.shandle(w));
	}
    }

    public static class Interact extends ItemEvent {
	public Interact(Coord c, ItemDrag src) {super(c, src);}
	public Interact(Interact from, Coord c) {super(from, c);}
	public Interact derive(Coord c) {return(new Interact(this, c));}

	protected boolean shandle(Widget w) {
	    if((w != src) && (w instanceof DTarget) && ((DTarget)w).iteminteract(this))
		return(true);
	    return(super.shandle(w));
	}
    }
}
