package com.kinyshu.acegui.vendor;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;

public class Primitive {

    public static class Line {
        public static void line(int startX, int startY, int endX, int endY, int width, Color color) {

            int factor = ScaleFactor.getFactor();
            {
                startX /= factor;
                startY /= factor;
                endX /= factor;
                endY /= factor;
            }

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glLineWidth(width);

            Primitive.color(color);
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2i(startX, startY);
                GL11.glVertex2i(endX, endY);
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public static class Rect {

        public static void filled(int left, int top, int right, int bottom, Color color) {

            int factor = ScaleFactor.getFactor();
            {
                left /= factor;
                top /= factor;
                right /= factor;
                bottom /= factor;
            }

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Primitive.color(color);

            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2i(left, bottom);
                GL11.glVertex2i(right, bottom);
                GL11.glVertex2i(right, top);
                GL11.glVertex2i(left, top);
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
        }

        public static void outline(int x, int y, int w, int h, int width, Color color) {

            GL11.glLineWidth(width);
            {
                Line.line(x, y, x + w, y, 1, color);
                Line.line(x + w, y, x + w, y + h,1, color);
                Line.line(x, y + h, x + w, y + h,1, color);
                Line.line(x, y, x, y + h,1, color);
            }
        }

        public static void smoothed(int x, int y, int w, int h, Color color){
            Rect.outline(x, y, w, h, 2, color);
            {
                Rect.filled(x, y, x + w - 1, x + h - 1, color);
            }
        }

        public static void rounded(int x, int y, int w, int h, int round,
           boolean topLeft, boolean topRight, boolean leftBottom, boolean rightBottom, Color color) {

            Rect.filled(x, y + round, x + w, y + h - round, color);
            Rect.filled(x + round, y, x + w - round, y + h, color);

            if ( topLeft ) {
                Circle.filled(x, y, round, 48, color);
            }
            else {
                Rect.filled(x, y, x + round, y + round, color);
            }

            if ( topRight ) {
                Circle.filled(x + (w - round * 2), y, round, 48, color);
            }
            else {
                Rect.filled(x + w, y, x + w - round, y + round, color);
            }

            if ( leftBottom ) {
                Circle.filled(x, y + (h - round * 2), round, 48, color);
            }
            else {
                Rect.filled(x, y + h, x + round, y + h - round, color);
            }

            if ( rightBottom ) {
                Circle.filled(x + (w - round * 2), y + (h - round * 2), round, 48, color);
            }
            else {
                Rect.filled(x + w, y + h, x + w - round, y + h - round, color);
            }
        }
    }

    public static class Circle {

        public static void filled(int x, int y, int radius, int points, Color color) {

            int factor = ScaleFactor.getFactor();
            {
                x /= factor;
                y /= factor;
                radius /= factor;
            }

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Primitive.color(color);
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            {
                for (double i = 0; i <= points; i++) {
                    double angle = i * (Math.PI * 2) / points;
                    GL11.glVertex2d(x + (radius * Math.cos(angle)) + radius, y + (radius * Math.sin(angle)) + radius);
                }
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        public static void outline(int x, int y, int radius, int points, float width, Color color) {

            int factor = ScaleFactor.getFactor();
            {
                x /= factor;
                y /= factor;
                radius /= factor;
            }

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glLineWidth(width);

            Primitive.color(color);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            {
                for (double i = 0; i <= points; i++) {
                    double angle = i * (Math.PI * 2) / points;
                    GL11.glVertex2d(x + (radius * Math.cos(angle)) + radius, y + (radius * Math.sin(angle)) + radius);
                }
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        public static void smoothed(int x, int y, int radius, Color color) {

            Circle.outline(x, y, radius, 98, 2.f, color);
            {
                Circle.filled(x, y, radius, 98, color);
            }
        }
    }

    public static void color(Color color) {
        GL11.glColor4f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f, color.getAlpha() / 255.f);
    }
}
