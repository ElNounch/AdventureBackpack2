package com.darkona.adventurebackpack.client.gui;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;

import com.darkona.adventurebackpack.common.Constants.Source;
import com.darkona.adventurebackpack.config.ConfigHandler;
import com.darkona.adventurebackpack.inventory.ContainerCopter;
import com.darkona.adventurebackpack.inventory.InventoryCopterPack;
import com.darkona.adventurebackpack.reference.GeneralReference;
import com.darkona.adventurebackpack.util.Resources;

/**
 * Created on 03/01/2015
 *
 * @author Darkona
 */
public class GuiCopterPack extends GuiWithTanks
{
    private static final ResourceLocation TEXTURE = Resources.guiTextures("guiCopterPack");

    private static GuiImageButtonNormal equipButton = new GuiImageButtonNormal(150, 64, 18, 18);
    private static GuiImageButtonNormal unequipButton = new GuiImageButtonNormal(150, 64, 18, 18);
    private static GuiTank fuelTank = new GuiTank(8, 8, 72, 32, ConfigHandler.typeTankRender);

    private InventoryCopterPack inventory;

    public GuiCopterPack(EntityPlayer player, InventoryCopterPack inv, Source source)
    {
        super(new ContainerCopter(player, inv, source));
        this.player = player;
        inventory = inv;
        this.source = source;
        xSize = 176;
        ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY)
    {
        GL11.glColor4f(1, 1, 1, 1);
        this.mc.renderEngine.bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (source == Source.WEARING)
        {
            if (unequipButton.inButton(this, mouseX, mouseY))
            {
                unequipButton.draw(this, 20, 186);
            }
            else
            {
                unequipButton.draw(this, 1, 186);
            }
        }
        else if (source == Source.HOLDING)
        {
            if (equipButton.inButton(this, mouseX, mouseY))
            {
                equipButton.draw(this, 20, 167);
            }
            else
            {
                equipButton.draw(this, 1, 167);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        inventory.openInventory();
        FluidTank tank = inventory.getFuelTank();
        fuelTank.draw(this, tank);

        GL11.glPushMatrix();
        boolean isFilled = tank.getFluid() != null;
        String name = isFilled ? WordUtils.capitalize(tank.getFluid().getFluid().getName()) : "None";
        String amount = isFilled ? "" + tank.getFluid().amount : "0";
        String capacity = Integer.toString(tank.getCapacity());
        int offsetY = 8;
        int offsetX = 83;
        fontRendererObj.drawString(name, 1 + offsetX, offsetY, 0x373737, false);
        fontRendererObj.drawString(amount, 1 + offsetX, 10 + offsetY, 0x373737, false);
        fontRendererObj.drawString(capacity, 1 + offsetX, 20 + offsetY, 0x373737, false);

        if (isFilled)
        {
            Float rate = GeneralReference.getFuelRate(name.toLowerCase());
            String conLev = (rate != null) ? rate.toString() : "0";
            fontRendererObj.drawString("Consumption: " + conLev, 1 + offsetX, 40 + offsetY, 0x373737, false);
        }

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    @Override
    protected GuiImageButtonNormal getEquipButton()
    {
        return equipButton;
    }

    @Override
    protected GuiImageButtonNormal getUnequipButton()
    {
        return unequipButton;
    }
}
