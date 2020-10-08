package com.wms.base.api;

import android.os.Build;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

final public class DataAPI {

    private final static String NULL = "null";

    public static boolean isEmpty(CharSequence sequence) {
        return TextUtils.isEmpty(sequence) || sequence.toString().toLowerCase().equals(NULL);
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return getSize(map) == 0;
    }

    public static <T> boolean isEmpty(Set<T> set) {
        return getSize(set) == 0;
    }

    public static <T> boolean isEmpty(List<T> list) {
        return getSize(list) == 0;
    }

    public static <T> boolean isEmpty(T[] array) {
        return getSize(array) == 0;
    }

    public static boolean isEmpty(int[] ints) {
        return getSize(ints) == 0;
    }

    public static <E> boolean isEmpty(Stack<E> stack) {
        return getSize(stack) == 0;
    }

    public static <E> int getSize(Stack<E> stack) {
        if(stack == null) {
            return 0;
        }
        return stack.size();
    }

    public static <E> void removeElement(Stack<E> stack, E e) {
        if(isEmpty(stack)) {
            return;
        }
        if(e == null) {
            return;
        }
        stack.remove(e);
    }

    public static <E> E getTopElement(Stack<E> stack) {
        if(isEmpty(stack)) {
            return null;
        }
        return stack.firstElement();
    }

    public static <K, V> int getSize(Map<K, V> map) {
        if (map == null) {
            return 0;
        }
        return map.size();
    }

    public static <T> int getSize(List<T> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static <T> int getSize(Set<T> set) {
        if (set == null) {
            return 0;
        }
        return set.size();
    }

    public static <T> int getSize(T[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    public static int getSize(int[] ints) {
        if(ints == null) {
            return 0;
        }
        return ints.length;
    }

    public static <T> T getBean(List<T> list, int index) {
        if (getSize(list) < index + 1) {
            return null;
        }
        return list.get(index);
    }

    public static <T> T getBean(T[] array, int index) {
        if (getSize(array) < index + 1) {
            return null;
        }
        return array[index];
    }

    public static int getInt(int[] ints, int index) {
        if(getSize(ints) < index + 1) {
            return -1;
        }
        return ints[index];
    }

    public static <K> boolean getBoolean(Map<K, Boolean> map, K k) {
        if (isEmpty(map)) {
            return false;
        }
        if (k == null) {
            return false;
        }
        if (map.get(k) == null) {
            return false;
        }
        return map.get(k);
    }

    public static boolean isEqual(CharSequence a, CharSequence b) {
        return !isEmpty(a) && !isEmpty(b) && a.equals(b);
    }

    public static boolean isMoreN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static <N> boolean hasNull(N... ns) {
        for(N n: ns) {
            if(n == null) {
                return true;
            }
        }
        return false;
    }
}
