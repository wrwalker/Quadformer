package com.williamrobertwalker.quadformer.GameObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.williamrobertwalker.quadformer.GameView;
import com.williamrobertwalker.quadformer.VectorMethods;

/**
 * Created by TheWo_000 on 2/7/2016.
 */
public class Entity {
    public PointF location;
    public PointF velocity = new PointF();
    public int width;
    public int height;
    public Paint paint;
    public PointF max, min;

    PointF[] vertices = new PointF[4];

    Paint shadowPaint = new Paint();

    //Constants
    //originally 0.3f
    private final float GRAVITY_CONSTANT = (1500f/GameView.diagonalScreenSize);
    private final float JUMP_VELOCITY = 25;
    final float MAXSPEED = 20;



    public Entity(PointF location, int width, int height, int color)
    {
//        GRAVITY_CONSTANT = GameView.screenHeight * (0.111f / 200);
        this.location = location;
        this.width = width;
        this.height = height;
        this.paint = new Paint();
        paint.setColor(color);
        min = location;
        max = new PointF(location.x + width, location.y + height);
        this.shadowPaint.setColor(GameView.shadowColor);
    }

    /**
     * Updates the entity
     */
    public void update()
    {
        //Update fall and move so it falls and moves.
        fall();
        move();

        //For every ground Tile, check if this guy is touching them.
        for(int i = 0; i < GameView.groundTileList.size(); i++) //TODO: FIXME. Try adding a distance calculation to avoid checking EVERY SINGLE TILE IN THE GAME!
        {
            if(this.isTouchingGroundTile(GameView.groundTileList.get(i))) {
                //If its touching the tile, but not the side, then it's obviously touching the top or bottom,
                //so stop this from moving up or down.
                if(!isTouchingSide(GameView.groundTileList.get(i)))
                {
                    this.velocity.y = 0;
                }
                collisionResponse(GameView.groundTileList.get(i));
            }
        }
    }

    //TODO: FIXME Mostly working, except cannot jump if character is more than half off of a tile.
    private boolean isTouchingSide(GroundTile groundTile)
    {
        return Math.abs((this.location.x + this.width/2) - (groundTile.location.x + groundTile.getWidth()/2)) > (groundTile.getWidth()/2);
    }

    private boolean isTouchingTop(GroundTile groundTile)
    {
        //Mostly Broken if touching top. Like, why is there a 7 there? Did 6 not work?
        return (this.location.x > (groundTile.location.x + 7)) || ((this.location.x + this.width) < (groundTile.location.x + (groundTile.getWidth() - 7)));
    }

    /**
     * Draws the entity to the canvas
     * @param canvas The canvas which to draw the entity on.
     */
    public void draw(Canvas canvas)
    {
        canvas.drawRect(location.x - GameView.viewOffset.x, location.y - GameView.viewOffset.y, (location.x + width) - GameView.viewOffset.x, (location.y + height) - GameView.viewOffset.y, paint);
    }



    PointF currentVertex;
    PointF nextVertex;
    PointF edge;
    PointF normal = new PointF();
    PointF lightToCurrent;

    PointF point1;
    PointF point2;

    volatile PointF tempVector = new PointF();
    volatile PointF tempVector2 = new PointF();
    volatile PointF tempVector3 = new PointF();

    Path path = new Path();

    public void drawShadow(Canvas canvas)
    {
        vertices = this.getVertices();
        for(Light light : GameView.lights)
        {
            for(int i = 0; i < vertices.length; i++)
            {
                currentVertex = vertices[i];
                nextVertex = vertices[(i + 1) % vertices.length];
                edge = VectorMethods.sub(tempVector, nextVertex, currentVertex);
                normal.x = edge.y;
                normal.y = -edge.x;
                lightToCurrent = VectorMethods.sub(tempVector, currentVertex, light.location);

                if(VectorMethods.dot(normal, lightToCurrent) > 0)
                {
                    //TODO: Make me more efficient.
                    point1 = VectorMethods.scale(tempVector2, VectorMethods.add(tempVector, currentVertex, VectorMethods.sub(tempVector, currentVertex, VectorMethods.scale(tempVector, (light.location), 2))), 100);
                    point2 = VectorMethods.scale(tempVector3, VectorMethods.add(tempVector, nextVertex, VectorMethods.sub(tempVector, nextVertex, VectorMethods.scale(tempVector, (light.location), 2))), 100);

                    //Drawing the shadow quads.
                    path.reset();
                    path.moveTo(currentVertex.x - GameView.viewOffset.x, currentVertex.y - GameView.viewOffset.y);
                    path.lineTo(point2.x - GameView.viewOffset.x, point2.y - GameView.viewOffset.y);
                    path.lineTo(point1.x - GameView.viewOffset.x, point1.y - GameView.viewOffset.y);
                    path.lineTo(nextVertex.x - GameView.viewOffset.x, nextVertex.y - GameView.viewOffset.y);
                    canvas.drawPath(path, shadowPaint);
                }
            }
        }
    }

    private void move() {
        this.location.x += this.velocity.x;
        this.location.y += this.velocity.y;
        this.min = location;
//        this.max.set(location.x + width, location.y + height);
        this.max.x = location.x + width;
        this.max.y = location.y + height;
    }

    private void fall()
    {
        if(this.velocity.y <= MAXSPEED) {
            this.velocity.y += GRAVITY_CONSTANT;
        }
    }

    /**
     * Is this touching the ground tile?
     * @param groundTile The ground tile in question
     * @return true or false?
     */
    private boolean isTouchingGroundTile(GroundTile groundTile) {

        //Return true if  !(furthest right side < groundtile furthest left side) || (furthest left side > groundtile furthest right side)
        //So if they miss eachother on x values.
        //Its a similar case with y values.

        return !(((this.location.x + width) < (groundTile.location.x) || (this.location.x) > (groundTile.location.x + groundTile.getWidth())))
                && !(((this.location.y + height) < (groundTile.location.y) || (this.location.y) > (groundTile.location.y + groundTile.getHeight())));


    }


    //TODO: FIXME
    /**
     * A response to what happens if the player is overlapping a ground tile.
     * It will figure out the penetration delta, or how this is penetrating the object.
     * @param groundTile The ground tile in question.
     */
    private void collisionResponse(GroundTile groundTile)
    {
        //New locations to make it easier to do the math with the locations in the middle of the boxes.
        PointF newLocation = new PointF(location.x + width/2, location.y + height/2);
        PointF newGroundTileLocation = new PointF(groundTile.location.x + groundTile.getWidth()/2, groundTile.location.y + groundTile.getHeight()/2);

        PointF newRadius = new PointF(this.width/2, this.height/2);

        //Yes, I know rectangles don't have radii. It's the width from the center to the right edge and the height of the center to the top.
        PointF groundTileRadius = new PointF(groundTile.getWidth()/2, groundTile.getHeight()/2);

        PointF delta = new PointF(newLocation.x - newGroundTileLocation.x, newLocation.y - newGroundTileLocation.y);//tile->obj delta. So the change in (delta) or the difference in position of groundTile and object.

        PointF penetration = new PointF((groundTileRadius.x + newRadius.x) - Math.abs(delta.x), (groundTileRadius.y + newRadius.y) - Math.abs(delta.y));//penetration depth in x and y

        //project in x
        if(penetration.x < penetration.y)
        {

            if(delta.x < 0)
            {
                //project to the left
                penetration.x *= -1;
                penetration.y = 0;
            }
            else
            {
                //project to right
                penetration.y = 0;
            }
        }
        else //project in y
        {

            if(delta.y < 0)
            {
                //project up
                penetration.x = 0;
                penetration.y *= -1;
            }
            else
            {
                //project down
                penetration.x = 0;
            }
        }


        //Solve the thing using the penetration variable as a penetration resolution.
//        float normaizationDenominator = (float)Math.sqrt(penetration.x * penetration.x + penetration.y * penetration.y);

        ReportCollisionVsWorld(penetration, penetration.x, penetration.y);
//        ReportCollisionVsWorld(penetration, penetration.x/normaizationDenominator, penetration.y/normaizationDenominator);

//        this.location.x += (penetration.x/normaizationDenominator);//project object out of collision
//        this.location.y += (penetration.y/normaizationDenominator);
    }

    /**
     * This finally resolves the collision
     * @param projectionVector The penetration vector
     * @param dx surface normal x component. Currently unused.
     * @param dy surface normal y component. Currently unused.
     */
    void ReportCollisionVsWorld(PointF projectionVector, float dx, float dy)
    {
        //calc velocity
//        PointF velocity = new PointF(this.location.x - this.oldLocation.x, this.location.y - this.oldLocation.y);
//
//        //find component of velocity parallel to collision normal
//        float dp = (velocity.x * dx + velocity.y * dy);
//        float normalX = dp * dx;//project velocity onto collision normal
//
//        float normalY = dp * dy;//nx,ny is normal velocity
//
//        float tangentX = velocity.x - normalX;//penetration.x,penetration.y is tangent velocity
//        float tangentY = velocity.y - normalY;
//
//        //we only want to apply collision response forces if the object is travelling into, and not out of, the collision
//        PointF bounce = new PointF();
//        PointF friction = new PointF();
//
//        if(dp < 0)
//        {
//
//            friction.x = tangentX * FRICTIONCONSTANT;
//            friction.y = tangentY * FRICTIONCONSTANT;
//
//            BOUNCECONSTANT = 1 + BOUNCECONSTANT;//this bounce constant should be elsewhere, i.e inside the object/tile/etc..
//
//            bounce.x = (normalX * BOUNCECONSTANT);
//            bounce.y = (normalY * BOUNCECONSTANT);
//            //Use these however you want.
//
//        }
//        else
//        {
//            //moving out of collision, do not apply forces
//            bounce.x = bounce.y = friction.x = friction.y = 0;
//        }


        this.location.x += projectionVector.x;//project object out of collision
        this.location.y += projectionVector.y;

//        this.oldLocation.x += projectionVector.x + bounce.x + friction.x;//apply bounce+friction impulses which alter velocity
//        this.oldLocation.y += projectionVector.y + bounce.y + friction.y;

    }

    public void jump()
    {

        //Check if its not moving up or down, and if that's the case then this is ready to jump, so jump!
        if(velocity.y == 0) {
            this.velocity.y = -JUMP_VELOCITY;
        }
    }

    PointF[] getVertices() {
        return new PointF[] {
                new PointF(this.location.x, this.location.y),
                new PointF(this.location.x, this.location.y + height),
                new PointF(this.location.x + width, this.location.y + height),
                new PointF(this.location.x + width, this.location.y)
        };
    }
}
