/*
Faolan Project, a free Age of Conan server emulator made for educational purpose
Copyright (C) 2008 Project Faolan team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

#include "Packet.h"
#include <iostream>

Packet::Packet(Buffer* buf) :
    m_buffer(buf)
{
    //size check are made in the packet parser engine, no need to check data here.
    (*buf) >> m_size >> m_sender >> m_unknown1 >> m_unknown2 >> m_receiver >> m_unknown3
            >> m_unknown4 >> m_opcode;
    
    m_size+=4;
}

// size check in new packet have to be done by higher level packet
// considering the data size
Packet::Packet(uint32 size, const std::string& sender, uint32 unknown1,
        uint32 unknown2, const std::string& receiver, uint32 unknown3,
        uint32 unknown4, uint32 opcode, Buffer* buf) :
    m_buffer(buf), m_sender(sender), m_receiver(receiver), m_size(size), m_opcode(opcode),
            m_unknown1(unknown1), m_unknown2(unknown2), m_unknown3(unknown3),
            m_unknown4(unknown4)
{
    (*buf) << (m_size-4) << m_sender << m_unknown1 << m_unknown2 << m_receiver
            << m_unknown3 << m_unknown4 << m_opcode;
}

///////////////////////////////////////////////////
// Getters def
//////////////////////////////////////////////////

uint32 Packet::opcode()
{
    return m_opcode;
}

uint32 Packet::unknown1()
{
    return m_unknown1;
}

uint32 Packet::unknown2()
{
    return m_unknown2;
}

uint32 Packet::unknown3()
{
    return m_unknown3;
}

uint32 Packet::unknown4()
{
    return m_unknown4;
}

uint32 Packet::size()
{
    return m_size;
}

std::string Packet::receiver()
{
    return m_receiver;
}

std::string Packet::sender()
{
    return m_sender;
}




